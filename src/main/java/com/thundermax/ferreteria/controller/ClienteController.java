package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.ClienteRequest;
import com.thundermax.ferreteria.model.Cliente;
import com.thundermax.ferreteria.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "4. Clientes", description = "Compradores en la ferretería")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Lista todos los clientes")
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un cliente por su ID interno")
    public Cliente obtener(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @GetMapping("/nit/{nit}")
    @Operation(summary = "Busca un cliente por su NIT")
    public Cliente obtenerPorNit(@PathVariable String nit) {
        return clienteService.obtenerPorNit(nit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra un nuevo cliente")
    public Cliente crear(@Valid @RequestBody ClienteRequest req) {
        return clienteService.crear(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza los datos de un cliente")
    public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return clienteService.actualizar(id, req);
    }
}
