package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.model.Venta;
import com.thundermax.ferreteria.model.enums.EstadoVenta;
import com.thundermax.ferreteria.repository.ProductoRepository;
import com.thundermax.ferreteria.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportesService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public Map<String, Object> ventasDelDia(LocalDate fecha) {
        List<Venta> ventas = ventaRepository.findByFechaBetweenOrderByFechaDesc(
                fecha.atStartOfDay(),
                fecha.atTime(23, 59, 59)
        );

        long cantidadVentas = ventas.stream().filter(v -> v.getEstado() == EstadoVenta.COMPLETADA).count();
        BigDecimal totalIngresos = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.COMPLETADA)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("fecha", fecha);
        reporte.put("ventasCompletadas", cantidadVentas);
        reporte.put("totalIngresos", totalIngresos);
        return reporte;
    }

    public List<Map<String, Object>> topProductos() {
        List<Object[]> resultados = ventaRepository.topProductos(EstadoVenta.COMPLETADA);
        List<Map<String, Object>> top = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("productoId", fila[0]);
            map.put("codigo", fila[1]);
            map.put("nombre", fila[2]);
            map.put("cantidadVendida", fila[3]);
            map.put("totalGenerado", fila[4]);
            top.add(map);
        }
        return top;
    }

    public Map<String, Object> valorInventario() {
        BigDecimal total = productoRepository.calcularValorInventario();
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("concepto", "Valor total de la mercadería en stock (Precio de Venta sin IVA)");
        reporte.put("total", total);
        return reporte;
    }
}
