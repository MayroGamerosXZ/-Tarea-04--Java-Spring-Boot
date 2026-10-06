package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.ProveedorRequest;
import com.thundermax.ferreteria.model.Proveedor;
import com.thundermax.ferreteria.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "2. Proveedores", description = "Empresas que surten a la ferretería")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @Operation(summary = "Lista todos los proveedores")
    public List<Proveedor> listar() {
        return proveedorService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un proveedor por ID")
    public Proveedor obtener(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un nuevo proveedor")
    public Proveedor crear(@Valid @RequestBody ProveedorRequest req) {
        return proveedorService.crear(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un proveedor")
    public Proveedor actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest req) {
        return proveedorService.actualizar(id, req);
    }
}
