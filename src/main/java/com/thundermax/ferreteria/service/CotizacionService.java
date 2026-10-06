package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.config.ThunderMaxProperties;
import com.thundermax.ferreteria.dto.CotizacionRequest;
import com.thundermax.ferreteria.dto.DetalleCotizacionRequest;
import com.thundermax.ferreteria.dto.DetalleVentaRequest;
import com.thundermax.ferreteria.dto.VentaRequest;
import com.thundermax.ferreteria.exception.RecursoNoEncontradoException;
import com.thundermax.ferreteria.exception.ReglaNegocioException;
import com.thundermax.ferreteria.model.*;
import com.thundermax.ferreteria.model.enums.EstadoCotizacion;
import com.thundermax.ferreteria.model.enums.MetodoPago;
import com.thundermax.ferreteria.repository.CotizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final ThunderMaxProperties props;
    private final VentaService ventaService;

    public List<Cotizacion> listar() {
        return cotizacionRepository.findAllByOrderByFechaDesc();
    }

    public Cotizacion obtenerPorId(Long id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotización", id));
    }

    @Transactional
    public Cotizacion crear(CotizacionRequest req) {
        Cliente cliente = clienteService.obtenerPorId(req.clienteId());
        LocalDateTime ahora = LocalDateTime.now();

        Cotizacion cot = Cotizacion.builder()
                .fecha(ahora)
                .fechaVencimiento(ahora.toLocalDate().plusDays(req.diasVigencia()))
                .cliente(cliente)
                .estado(EstadoCotizacion.VIGENTE)
                .observaciones(req.observaciones())
                .subtotal(BigDecimal.ZERO).iva(BigDecimal.ZERO).total(BigDecimal.ZERO)
                .build();

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (DetalleCotizacionRequest dReq : req.detalles()) {
            Producto p = productoService.obtenerEntidad(dReq.productoId());
            BigDecimal sub = p.getPrecio().multiply(new BigDecimal(dReq.cantidad()));
            subtotalAcumulado = subtotalAcumulado.add(sub);

            DetalleCotizacion detalle = DetalleCotizacion.builder()
                    .producto(p).cantidad(dReq.cantidad())
                    .precioUnitario(p.getPrecio()).subtotal(sub)
                    .build();
            cot.agregarDetalle(detalle);
        }

        cot.setSubtotal(subtotalAcumulado);
        cot.setIva(props.calcularIva(subtotalAcumulado));
        cot.setTotal(cot.getSubtotal().add(cot.getIva()));

        return cotizacionRepository.save(cot);
    }

    /**
     * MAGIA: Toma una cotización, la convierte en una solicitud de Venta
     * y llama a VentaService. Si el stock no alcanza, VentaService lanzará error
     * y la transacción se aborta entera (incluyendo el cambio de estado de la cotización).
     */
    @Transactional
    public Venta convertirAVenta(Long id, MetodoPago pago) {
        Cotizacion cot = obtenerPorId(id);

        if (cot.getEstado() != EstadoCotizacion.VIGENTE) {
            throw new ReglaNegocioException("La cotización no está vigente. Estado: " + cot.getEstado());
        }

        // Armamos el request de venta usando los datos de la cotización
        List<DetalleVentaRequest> detallesVenta = cot.getDetalles().stream()
                .map(d -> new DetalleVentaRequest(d.getProducto().getId(), d.getCantidad()))
                .toList();

        VentaRequest vReq = new VentaRequest(cot.getCliente().getId(), pago, detallesVenta);

        // Llamamos al servicio de ventas (él se encarga de descontar stock, chequear kárdex, etc.)
        Venta venta = ventaService.registrarVenta(vReq);

        // Relacionamos ambas tablas
        venta.setCotizacionId(cot.getId());
        cot.setVentaId(venta.getId());
        cot.setEstado(EstadoCotizacion.CONVERTIDA);

        cotizacionRepository.save(cot);
        return venta;
    }
}
