package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.CotizacionRequest;
import com.thundermax.ferreteria.model.Cotizacion;
import com.thundermax.ferreteria.model.Venta;
import com.thundermax.ferreteria.model.enums.MetodoPago;
import com.thundermax.ferreteria.service.CotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
@Tag(name = "7. Cotizaciones", description = "Proformas y conversión a ventas")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @GetMapping
    @Operation(summary = "Lista todas las cotizaciones")
    public List<Cotizacion> listar() {
        return cotizacionService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Muestra el detalle de una cotización")
    public Cotizacion obtener(@PathVariable Long id) {
        return cotizacionService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea una nueva cotización (no descuenta stock)")
    public Cotizacion crear(@Valid @RequestBody CotizacionRequest req) {
        return cotizacionService.crear(req);
    }

    @PostMapping("/{id}/convertir-venta")
    @Operation(summary = "🪄 MAGIA: Convierte la cotización en Venta real. Valida stock y lo descuenta.")
    public Venta convertir(
            @PathVariable Long id,
            @Parameter(description = "Método de pago a utilizar") @RequestParam MetodoPago metodoPago) {
        return cotizacionService.convertirAVenta(id, metodoPago);
    }
}
