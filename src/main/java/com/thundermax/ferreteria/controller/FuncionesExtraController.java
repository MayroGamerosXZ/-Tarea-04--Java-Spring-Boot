package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.CalculoAreaRequest;
import com.thundermax.ferreteria.dto.SugerenciaMaterialResponse;
import com.thundermax.ferreteria.service.CalculadoraService;
import com.thundermax.ferreteria.service.ReportesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "8. Calculadora y Reportes", description = "Herramientas de ferretería y cuadros de mando")
public class FuncionesExtraController {

    private final CalculadoraService calculadoraService;
    private final ReportesService reportesService;

    // ----- Calculadora -----

    @PostMapping("/calculadora/pintura")
    @Operation(summary = "🧮 Calcula cuántos galones de pintura necesitas para un área y sugiere productos")
    public SugerenciaMaterialResponse calcularPintura(@Valid @RequestBody CalculoAreaRequest req) {
        return calculadoraService.calcularPintura(req);
    }

    @PostMapping("/calculadora/cemento")
    @Operation(summary = "🧮 Calcula cuántas bolsas de cemento necesitas para un área y sugiere productos")
    public SugerenciaMaterialResponse calcularCemento(@Valid @RequestBody CalculoAreaRequest req) {
        return calculadoraService.calcularCemento(req);
    }

    // ----- Reportes -----

    @GetMapping("/reportes/ventas-hoy")
    @Operation(summary = "📊 Resumen de ventas e ingresos del día de hoy")
    public Map<String, Object> ventasHoy() {
        return reportesService.ventasDelDia(LocalDate.now());
    }

    @GetMapping("/reportes/top-productos")
    @Operation(summary = "📊 Ranking de los productos más vendidos")
    public List<Map<String, Object>> topProductos() {
        return reportesService.topProductos();
    }

    @GetMapping("/reportes/valor-inventario")
    @Operation(summary = "📊 Calcula cuánto dinero hay en total almacenado en bodega")
    public Map<String, Object> valorInventario() {
        return reportesService.valorInventario();
    }
}
