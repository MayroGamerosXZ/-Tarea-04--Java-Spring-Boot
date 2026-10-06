package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.VentaRequest;
import com.thundermax.ferreteria.model.Venta;
import com.thundermax.ferreteria.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "6. Ventas", description = "Facturación, control de stock y anulaciones")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Lista todas las ventas ordenadas por fecha")
    public List<Venta> listar() {
        return ventaService.listarVentas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Muestra el detalle de una venta específica")
    public Venta obtener(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra una venta (valida existencias, descuenta stock y cobra IVA)")
    public Venta crear(@Valid @RequestBody VentaRequest req) {
        return ventaService.registrarVenta(req);
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anula una venta y devuelve todos los productos al inventario")
    public Venta anular(
            @PathVariable Long id,
            @Parameter(description = "Motivo de la anulación") @RequestParam String motivo) {
        return ventaService.anularVenta(id, motivo);
    }
}
