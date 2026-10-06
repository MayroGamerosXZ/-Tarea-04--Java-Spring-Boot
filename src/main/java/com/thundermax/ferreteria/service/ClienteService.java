package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.ClienteRequest;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.Cliente;
import com.thundermax.ferreteria.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }

    public Cliente obtenerPorNit(String nit) {
        return clienteRepository.findByNitIgnoreCase(nit)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe cliente con NIT " + nit));
    }

    public Cliente crear(ClienteRequest req) {
        if (clienteRepository.existsByNitIgnoreCase(req.nit())) {
            throw new ReglaNegocioException("Ya existe un cliente con el NIT: " + req.nit());
        }

        Cliente c = Cliente.builder()
                .nombre(req.nombre())
                .nit(req.nit().toUpperCase())
                .telefono(req.telefono())
                .email(req.email())
                .direccion(req.direccion())
                .build();
        return clienteRepository.save(c);
    }

    public Cliente actualizar(Long id, ClienteRequest req) {
        Cliente c = obtenerPorId(id);

        if (!c.getNit().equalsIgnoreCase(req.nit()) && clienteRepository.existsByNitIgnoreCase(req.nit())) {
            throw new ReglaNegocioException("Ya existe otro cliente con el NIT: " + req.nit());
        }

        c.setNombre(req.nombre());
        c.setNit(req.nit().toUpperCase());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
        return clienteRepository.save(c);
    }
}
