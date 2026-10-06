package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.CategoriaRequest;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.Categoria;
import com.thundermax.ferreteria.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para Categorías.
 * {@code @Service} permite que Spring lo inyecte en los controladores automáticamente.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    public Categoria crear(CategoriaRequest req) {
        if (categoriaRepository.existsByNombreIgnoreCase(req.nombre())) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + req.nombre());
        }

        Categoria cat = Categoria.builder()
                .nombre(req.nombre())
                .descripcion(req.descripcion())
                .build();
        return categoriaRepository.save(cat);
    }

    public Categoria actualizar(Long id, CategoriaRequest req) {
        Categoria cat = obtenerPorId(id);

        // Si cambia el nombre, revisar que no choque con otra que ya exista
        if (!cat.getNombre().equalsIgnoreCase(req.nombre()) &&
                categoriaRepository.existsByNombreIgnoreCase(req.nombre())) {
            throw new ReglaNegocioException("Ya existe otra categoría con el nombre: " + req.nombre());
        }

        cat.setNombre(req.nombre());
        cat.setDescripcion(req.descripcion());
        return categoriaRepository.save(cat);
    }
}
