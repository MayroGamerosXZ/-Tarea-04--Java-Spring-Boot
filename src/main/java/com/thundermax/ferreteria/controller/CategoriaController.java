package com.thundermax.ferreteria.controller;

import com.thundermax.ferreteria.dto.CategoriaRequest;
import com.thundermax.ferreteria.model.Categoria;
import com.thundermax.ferreteria.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Categorías.
 * Equivalente a: {@code router.get('/api/categorias', ...)} en Node.js.
 */
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "1. Categorías", description = "Catálogo de familias de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Lista todas las categorías")
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca una categoría por ID")
    public Categoria obtener(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created en vez de 200 OK
    @Operation(summary = "Crea una nueva categoría")
    public Categoria crear(@Valid @RequestBody CategoriaRequest req) {
        // @Valid ejecuta las reglas (@NotBlank, @Size) definidas en el DTO antes de entrar aquí
        return categoriaService.crear(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una categoría existente")
    public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest req) {
        return categoriaService.actualizar(id, req);
    }
}
