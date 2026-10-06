package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.ProductoRequest;
import com.thundermax.ferreteria.dto.ProductoResponse;
import com.thundermax.ferreteria.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "3. Productos", description = "Catálogo y búsqueda de artículos")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Lista todos los productos activos")
    public List<ProductoResponse> listar() {
        return productoService.listarTodos();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca productos que contengan un texto en su nombre")
    public List<ProductoResponse> buscar(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "🚨 Lista productos cuyo stock llegó a su límite mínimo")
    public List<ProductoResponse> stockBajo() {
        return productoService.listarStockBajo();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un producto por ID")
    public ProductoResponse obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un producto nuevo (con stock 0)")
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest req) {
        return productoService.crear(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un producto (no altera su stock)")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest req) {
        return productoService.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Elimina un producto (borrado lógico, queda inactivo)")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminarLogico(id);
    }
}
