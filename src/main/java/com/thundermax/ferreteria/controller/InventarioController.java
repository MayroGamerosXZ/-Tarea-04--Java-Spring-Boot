package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.EntradaInventarioRequest;
import com.thundermax.ferreteria.model.EntradaInventario;
import com.thundermax.ferreteria.model.MovimientoKardex;
import com.thundermax.ferreteria.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // La raíz principal, luego separamos rutas
@RequiredArgsConstructor
@Tag(name = "5. Inventario y Kárdex", description = "Control de entradas y bitácora de productos")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/entradas")
    @Operation(summary = "Lista todas las entradas de mercadería")
    public List<EntradaInventario> listarEntradas() {
        return inventarioService.listarEntradas();
    }

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra una entrada (compra a proveedor) y aumenta el stock")
    public EntradaInventario registrarEntrada(@Valid @RequestBody EntradaInventarioRequest req) {
        return inventarioService.registrarEntrada(req);
    }

    @GetMapping("/productos/{productoId}/kardex")
    @Operation(summary = "Obtiene la bitácora completa (Kárdex) de un producto")
    public List<MovimientoKardex> obtenerKardex(@PathVariable Long productoId) {
        return inventarioService.obtenerKardexProducto(productoId);
    }

    @PostMapping("/salidas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra una salida manual (merma, ajuste) reduciendo el stock")
    public void registrarSalidaManual(@Valid @RequestBody com.thundermax.ferreteria.dto.SalidaManualRequest req) {
        inventarioService.registrarSalidaManual(req);
    }
}
