package com.thundermax.ferreteria.service;

import com.thundermax.ferreteria.dto.CalculoAreaRequest;
import com.thundermax.ferreteria.dto.ProductoResponse;
import com.thundermax.ferreteria.dto.SugerenciaMaterialResponse;
import com.thundermax.ferreteria.model.enums.UnidadMedida;
import com.thundermax.ferreteria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculadoraService {

    private final ProductoRepository productoRepository;

    /**
     * Un galón de pintura rinde aprox. 35 metros cuadrados a 2 manos.
     */
    public SugerenciaMaterialResponse calcularPintura(CalculoAreaRequest req) {
        double area = req.area();
        // Math.ceil redondea hacia arriba (ej. 1.2 -> 2 galones)
        int galones = (int) Math.ceil(area / 35.0);

        List<ProductoResponse> sugerencias = productoRepository
                .findByActivoTrueAndUnidadAndNombreContainingIgnoreCaseOrderByPrecioAsc(UnidadMedida.GALON, "pintura")
                .stream()
                .filter(p -> p.getStock() >= galones) // Solo sugiere los que alcancen
                .map(ProductoResponse::de).toList();

        return new SugerenciaMaterialResponse("Pintura", area, galones, "Galón", sugerencias);
    }

    /**
     * Una bolsa de cemento (42.5kg) rinde aprox. 1.5 metros cuadrados para fundición/piso.
     */
    public SugerenciaMaterialResponse calcularCemento(CalculoAreaRequest req) {
        double area = req.area();
        int bolsas = (int) Math.ceil(area / 1.5);

        List<ProductoResponse> sugerencias = productoRepository
                .findByActivoTrueAndUnidadAndNombreContainingIgnoreCaseOrderByPrecioAsc(UnidadMedida.BOLSA, "cemento")
                .stream()
                .filter(p -> p.getStock() >= bolsas)
                .map(ProductoResponse::de).toList();

        return new SugerenciaMaterialResponse("Cemento", area, bolsas, "Bolsa", sugerencias);
    }
}
