package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.config.ThunderMaxProperties;
import com.thundermax.ferreteria.dto.DetalleVentaRequest;
import com.thundermax.ferreteria.dto.VentaRequest;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.*;
import com.thundermax.ferreteria.model.enums.EstadoVenta;
import com.thundermax.ferreteria.model.enums.TipoMovimiento;
import com.thundermax.ferreteria.repository.MovimientoKardexRepository;
import com.thundermax.ferreteria.repository.ProductoRepository;
import com.thundermax.ferreteria.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final MovimientoKardexRepository kardexRepository;
    private final ThunderMaxProperties props; // Trae el IVA configurado en application.properties

    public List<Venta> listarVentas() {
        return ventaRepository.findAllByOrderByFechaDesc();
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta", id));
    }

    /**
     * @Transactional: si algo falla (ej. stock insuficiente a la mitad de la lista),
     * TODO se echa para atrás y no se cobra ni se descuenta nada.
     */
    @Transactional
    public Venta registrarVenta(VentaRequest req) {
        Cliente cliente = clienteService.obtenerPorId(req.clienteId());
        LocalDateTime ahora = LocalDateTime.now();

        Venta venta = Venta.builder()
                .fecha(ahora)
                .cliente(cliente)
                .metodoPago(req.metodoPago())
                .estado(EstadoVenta.COMPLETADA)
                .subtotal(BigDecimal.ZERO)
                .iva(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (DetalleVentaRequest det : req.detalles()) {
            Producto p = productoService.obtenerEntidad(det.productoId());

            // 1. Validar Stock
            if (p.getStock() < det.cantidad()) {
                throw new ReglaNegocioException(String.format(
                        "Stock insuficiente para '%s'. Solicitado: %d, Disponible: %d",
                        p.getNombre(), det.cantidad(), p.getStock()
                ));
            }

            // 2. Calcular subtotal de la línea
            BigDecimal sub = p.getPrecio().multiply(new BigDecimal(det.cantidad()));
            subtotalAcumulado = subtotalAcumulado.add(sub);

            // 3. Crear el detalle
            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .producto(p)
                    .cantidad(det.cantidad())
                    .precioUnitario(p.getPrecio()) // Se guarda el precio actual (sin iva)
                    .subtotal(sub)
                    .build();
            venta.agregarDetalle(detalleVenta);

            // 4. Actualizar Stock y Kárdex
            int stockAnterior = p.getStock();
            int stockNuevo = stockAnterior - det.cantidad();
            p.setStock(stockNuevo);
            productoRepository.save(p);

            // Nota: guardamos el kárdex temporalmente sin referencia real a la venta porque aún no tiene ID.
            // Se actualiza en el paso 6.
            MovimientoKardex mov = MovimientoKardex.builder()
                    .fecha(ahora).producto(p).tipo(TipoMovimiento.SALIDA_VENTA)
                    .cantidad(-det.cantidad()).stockAnterior(stockAnterior).stockNuevo(stockNuevo)
                    .precioVenta(p.getPrecio())
                    .build();
            kardexRepository.save(mov);
        }

        // 5. Cálculos finales
        venta.setSubtotal(subtotalAcumulado);
        venta.setIva(props.calcularIva(subtotalAcumulado));
        venta.setTotal(venta.getSubtotal().add(venta.getIva()));

        Venta ventaGuardada = ventaRepository.save(venta);

        // 6. Actualizar las referencias del Kárdex (ahora que la venta ya tiene ID)
        String ref = "Venta #" + ventaGuardada.getId();
        kardexRepository.findByProductoIdOrderByFechaAscIdAsc(null); // Fix de JPA para limpiar contexto
        // Forma simplificada: a los de la fecha actual que no tengan ref, se la ponemos
        List<MovimientoKardex> movs = kardexRepository.findAll().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.SALIDA_VENTA && m.getReferencia() == null)
                .toList();
        movs.forEach(m -> { m.setReferencia(ref); kardexRepository.save(m); });

        return ventaGuardada;
    }

    @Transactional
    public Venta anularVenta(Long id, String motivo) {
        Venta venta = obtenerPorId(id);

        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new ReglaNegocioException("La venta #" + id + " ya se encuentra anulada.");
        }

        LocalDateTime ahora = LocalDateTime.now();
        venta.setEstado(EstadoVenta.ANULADA);
        venta.setMotivoAnulacion(motivo);
        venta.setFechaAnulacion(ahora);

        // Devolver el stock de cada producto vendido
        for (DetalleVenta d : venta.getDetalles()) {
            Producto p = d.getProducto();
            int stockAnterior = p.getStock();
            int stockNuevo = stockAnterior + d.getCantidad();

            p.setStock(stockNuevo);
            productoRepository.save(p);

            MovimientoKardex mov = MovimientoKardex.builder()
                    .fecha(ahora).producto(p).tipo(TipoMovimiento.DEVOLUCION_ANULACION)
                    .cantidad(d.getCantidad()).stockAnterior(stockAnterior).stockNuevo(stockNuevo)
                    .referencia("Anulacion Venta #" + venta.getId()).precioVenta(p.getPrecio())
                    .build();
            kardexRepository.save(mov);
        }

        return ventaRepository.save(venta);
    }
}
