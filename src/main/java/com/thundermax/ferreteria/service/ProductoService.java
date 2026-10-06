package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.ProductoRequest;
import com.thundermax.ferreteria.dto.ProductoResponse;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.Categoria;
import com.thundermax.ferreteria.model.Producto;
import com.thundermax.ferreteria.model.Proveedor;
import com.thundermax.ferreteria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc().stream()
                .map(ProductoResponse::de).toList();
    }

    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(nombre)
                .stream().map(ProductoResponse::de).toList();
    }

    public List<ProductoResponse> listarStockBajo() {
        return productoRepository.findStockBajo().stream()
                .map(ProductoResponse::de).toList();
    }

    public Producto obtenerEntidad(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
        if (!p.getActivo()) {
            throw new RecursoNoEncontradoException("El producto con id " + id + " está inactivo");
        }
        return p;
    }

    public ProductoResponse obtenerPorId(Long id) {
        return ProductoResponse.de(obtenerEntidad(id));
    }

    public ProductoResponse crear(ProductoRequest req) {
        if (productoRepository.existsByCodigoIgnoreCase(req.codigo())) {
            throw new ReglaNegocioException("Ya existe un producto con el código " + req.codigo());
        }

        Categoria cat = categoriaService.obtenerPorId(req.categoriaId());
        Proveedor prov = req.proveedorId() != null ? proveedorService.obtenerPorId(req.proveedorId()) : null;

        Producto p = Producto.builder()
                .codigo(req.codigo().toUpperCase())
                .nombre(req.nombre())
                .descripcion(req.descripcion())
                .marca(req.marca())
                .precio(req.precio())
                .stock(0) // Nace con 0 stock. Se llena haciendo Entradas de Inventario
                .stockMinimo(req.stockMinimo())
                .unidad(req.unidad())
                .categoria(cat)
                .proveedor(prov)
                .activo(true)
                .build();

        return ProductoResponse.de(productoRepository.save(p));
    }

    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        Producto p = obtenerEntidad(id);

        if (!p.getCodigo().equalsIgnoreCase(req.codigo()) && productoRepository.existsByCodigoIgnoreCase(req.codigo())) {
            throw new ReglaNegocioException("Ya existe otro producto con el código " + req.codigo());
        }

        p.setCodigo(req.codigo().toUpperCase());
        p.setNombre(req.nombre());
        p.setDescripcion(req.descripcion());
        p.setMarca(req.marca());
        p.setPrecio(req.precio());
        p.setStockMinimo(req.stockMinimo());
        p.setUnidad(req.unidad());
        p.setCategoria(categoriaService.obtenerPorId(req.categoriaId()));
        p.setProveedor(req.proveedorId() != null ? proveedorService.obtenerPorId(req.proveedorId()) : null);

        // El stock NO se actualiza por aquí
        return ProductoResponse.de(productoRepository.save(p));
    }

    public void eliminarLogico(Long id) {
        Producto p = obtenerEntidad(id);
        p.setActivo(false);
        productoRepository.save(p);
    }
}
