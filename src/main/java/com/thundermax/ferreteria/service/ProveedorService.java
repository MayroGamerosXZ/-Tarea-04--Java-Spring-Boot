package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.ProveedorRequest;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.Proveedor;
import com.thundermax.ferreteria.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", id));
    }

    public Proveedor crear(ProveedorRequest req) {
        if (req.nit() != null && proveedorRepository.existsByNit(req.nit())) {
            throw new ReglaNegocioException("Ya existe un proveedor con el NIT: " + req.nit());
        }

        Proveedor p = Proveedor.builder()
                .nombre(req.nombre())
                .nit(req.nit())
                .contacto(req.contacto())
                .telefono(req.telefono())
                .email(req.email())
                .direccion(req.direccion())
                .build();
        return proveedorRepository.save(p);
    }

    public Proveedor actualizar(Long id, ProveedorRequest req) {
        Proveedor p = obtenerPorId(id);

        if (req.nit() != null && !req.nit().equals(p.getNit()) && proveedorRepository.existsByNit(req.nit())) {
            throw new ReglaNegocioException("Ya existe otro proveedor con el NIT: " + req.nit());
        }

        p.setNombre(req.nombre());
        p.setNit(req.nit());
        p.setContacto(req.contacto());
        p.setTelefono(req.telefono());
        p.setEmail(req.email());
        p.setDireccion(req.direccion());
        return proveedorRepository.save(p);
    }
}
