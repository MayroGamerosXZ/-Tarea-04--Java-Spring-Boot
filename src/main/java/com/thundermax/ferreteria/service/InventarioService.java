package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.EntradaInventarioRequest;
import com.thundermax.ferreteria.model.EntradaInventario;
import com.thundermax.ferreteria.model.MovimientoKardex;
import com.thundermax.ferreteria.model.Producto;
import com.thundermax.ferreteria.model.Proveedor;
import com.thundermax.ferreteria.model.enums.TipoMovimiento;
import com.thundermax.ferreteria.repository.EntradaInventarioRepository;
import com.thundermax.ferreteria.repository.MovimientoKardexRepository;
import com.thundermax.ferreteria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final EntradaInventarioRepository entradaRepository;
    private final MovimientoKardexRepository kardexRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    public List<EntradaInventario> listarEntradas() {
        return entradaRepository.findAllByOrderByFechaDesc();
    }

    public List<MovimientoKardex> obtenerKardexProducto(Long productoId) {
        return kardexRepository.findByProductoIdOrderByFechaAscIdAsc(productoId);
    }

    /**
     * @Transactional es CRÍTICO: si falla la creación de la entrada,
     * NO se guarda el kárdex ni se sube el stock (rollback automático).
     */
    @Transactional
    public EntradaInventario registrarEntrada(EntradaInventarioRequest req) {
        Producto producto = productoService.obtenerEntidad(req.productoId());
        Proveedor proveedor = proveedorService.obtenerPorId(req.proveedorId());

        int stockAnterior = producto.getStock();
        int stockNuevo = stockAnterior + req.cantidad();
        BigDecimal costoTotal = req.costoUnitario().multiply(new BigDecimal(req.cantidad()));
        LocalDateTime ahora = LocalDateTime.now();

        // 1. Crear el registro de entrada
        EntradaInventario entrada = EntradaInventario.builder()
                .fecha(ahora)
                .producto(producto)
                .proveedor(proveedor)
                .cantidad(req.cantidad())
                .costoUnitario(req.costoUnitario())
                .costoTotal(costoTotal)
                .numeroFactura(req.numeroFactura())
                .observaciones(req.observaciones())
                .build();
        entrada = entradaRepository.save(entrada);

        // 2. Registrar el movimiento en el Kárdex
        String ref = "Entrada #" + entrada.getId();
        if (req.numeroFactura() != null) ref += " (Fac: " + req.numeroFactura() + ")";

        MovimientoKardex mov = MovimientoKardex.builder()
                .fecha(ahora)
                .producto(producto)
                .tipo(TipoMovimiento.ENTRADA)
                .cantidad(req.cantidad())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .referencia(ref)
                .costoUnitario(req.costoUnitario())
                .precioVenta(producto.getPrecio())
                .build();
        kardexRepository.save(mov);

        // 3. Actualizar el stock del producto
        producto.setStock(stockNuevo);
        productoRepository.save(producto);

        return entrada;
    }

    @Transactional
    public void registrarSalidaManual(com.thundermax.ferreteria.dto.SalidaManualRequest req) {
        Producto producto = productoService.obtenerEntidad(req.productoId());
        if (producto.getStock() < req.cantidad()) {
            throw new com.thundermax.ferreteria.exception.ReglaNegocioException("Stock insuficiente para realizar esta salida manual.");
        }
        int stockAnterior = producto.getStock();
        int stockNuevo = stockAnterior - req.cantidad();

        MovimientoKardex mov = MovimientoKardex.builder()
                .fecha(LocalDateTime.now())
                .producto(producto)
                .tipo(TipoMovimiento.SALIDA)
                .cantidad(req.cantidad())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .referencia("Ajuste Manual: " + (req.motivo() != null ? req.motivo() : "Sin motivo"))
                .costoUnitario(BigDecimal.ZERO)
                .precioVenta(producto.getPrecio())
                .build();
        kardexRepository.save(mov);

        producto.setStock(stockNuevo);
        productoRepository.save(producto);
    }
}
