package com.thundermax.ferreteria.config;

import com.thundermax.ferreteria.model.*;
import com.thundermax.ferreteria.model.enums.TipoMovimiento;
import com.thundermax.ferreteria.model.enums.UnidadMedida;
import com.thundermax.ferreteria.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Carga datos de ejemplo la PRIMERA vez que arranca la aplicación (si la BD está vacía).
 * {@code CommandLineRunner} = código que se ejecuta automáticamente al iniciar Spring Boot.
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor // Lombok genera el constructor → Spring inyecta los repositorios
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoKardexRepository kardexRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            log.info("⚡ ThunderMax: la base de datos ya tiene datos, no se cargan ejemplos.");
            return;
        }
        log.info("⚡ ThunderMax: cargando datos de ejemplo de la ferretería...");

        // ---------- Categorías ----------
        Categoria herramientas = cat("Herramientas", "Herramientas manuales y eléctricas");
        Categoria electricidad = cat("Electricidad", "Cables, tomacorrientes, iluminación");
        Categoria plomeria = cat("Plomería", "Tubería PVC, llaves y accesorios");
        Categoria pintura = cat("Pintura", "Pinturas, brochas y rodillos");
        Categoria construccion = cat("Construcción", "Cemento, block, hierro");
        Categoria tornilleria = cat("Tornillería y Fijación", "Clavos, tornillos, anclas");

        // ---------- Proveedores ----------
        Proveedor martillo = prov("Distribuidora El Martillo, S.A.", "1234567-8", "Luis Pérez", "2234-5678",
                "ventas@elmartillo.com.gt", "Zona 12, Ciudad de Guatemala");
        Proveedor electro = prov("ElectroGuate", "7654321-0", "Ana López", "2456-7890",
                "pedidos@electroguate.com.gt", "Zona 4, Mixco");
        Proveedor arcoiris = prov("Pinturas Arcoíris", "4567890-1", "Carlos Méndez", "2345-6789",
                "info@pinturasarcoiris.com.gt", "Villa Nueva");
        Proveedor cementos = prov("Cementos del Valle", "9876543-2", "María Juárez", "2290-1122",
                "ventas@cementosdelvalle.com.gt", "Km 18 Carretera al Atlántico");

        // ---------- Clientes ----------
        clienteRepository.save(Cliente.builder().nombre("Consumidor Final").nit(Cliente.NIT_CONSUMIDOR_FINAL).build());
        clienteRepository.save(Cliente.builder().nombre("Constructora Los Volcanes").nit("5551234-5")
                .telefono("5512-3456").email("compras@losvolcanes.com.gt").direccion("Antigua Guatemala").build());
        clienteRepository.save(Cliente.builder().nombre("Juan García").nit("8889990-1")
                .telefono("4123-4567").direccion("Zona 7, Guatemala").build());

        // ---------- Productos (precio SIN IVA, en quetzales) ----------
        prod("HER-001", "Martillo de uña 16 oz", "Truper", "65.00", 25, 5, UnidadMedida.UNIDAD, herramientas, martillo);
        prod("HER-002", "Desarmador de cruz #2", "Stanley", "22.50", 40, 10, UnidadMedida.UNIDAD, herramientas, martillo);
        prod("HER-003", "Cinta métrica 5 m", "Truper", "38.00", 4, 6, UnidadMedida.UNIDAD, herramientas, martillo);
        prod("HER-004", "Taladro percutor 1/2\" 600W", "Black+Decker", "495.00", 6, 2, UnidadMedida.UNIDAD, herramientas, martillo);
        prod("HER-005", "Llave ajustable 10\"", "Pretul", "55.00", 12, 4, UnidadMedida.UNIDAD, herramientas, martillo);

        prod("ELE-001", "Cable THHN calibre 12", "Condumex", "4.75", 300, 50, UnidadMedida.METRO, electricidad, electro);
        prod("ELE-002", "Tomacorriente doble polarizado", "Eagle", "18.00", 3, 10, UnidadMedida.UNIDAD, electricidad, electro);
        prod("ELE-003", "Foco LED 9W luz blanca", "Philips", "15.00", 80, 20, UnidadMedida.UNIDAD, electricidad, electro);

        prod("PLO-001", "Tubo PVC 1/2\" x 6 m", "Amanco", "32.00", 50, 10, UnidadMedida.UNIDAD, plomeria, martillo);
        prod("PLO-002", "Llave de paso 1/2\"", "FV", "45.00", 15, 5, UnidadMedida.UNIDAD, plomeria, martillo);
        prod("PLO-003", "Cinta teflón 1/2\"", "Truper", "3.50", 100, 20, UnidadMedida.ROLLO, plomeria, martillo);

        prod("PIN-001", "Pintura látex blanca", "Arcoíris", "125.00", 30, 8, UnidadMedida.GALON, pintura, arcoiris);
        prod("PIN-002", "Pintura látex blanca", "Arcoíris", "560.00", 8, 3, UnidadMedida.CUBETA, pintura, arcoiris);
        prod("PIN-003", "Pintura anticorrosiva negra", "Arcoíris", "145.00", 2, 5, UnidadMedida.GALON, pintura, arcoiris);
        prod("PIN-004", "Brocha 3\"", "Truper", "18.00", 35, 10, UnidadMedida.UNIDAD, pintura, arcoiris);
        prod("PIN-005", "Rodillo 9\" con bandeja", "Truper", "48.00", 20, 5, UnidadMedida.UNIDAD, pintura, arcoiris);

        prod("CON-001", "Cemento gris 42.5 kg", "Cementos del Valle", "82.00", 120, 30, UnidadMedida.BOLSA, construccion, cementos);
        prod("CON-002", "Block de concreto 15x20x40 cm", "Cementos del Valle", "5.25", 1500, 300, UnidadMedida.UNIDAD, construccion, cementos);
        prod("CON-003", "Varilla de hierro 3/8\" x 6 m", "Aceros de Guatemala", "42.00", 200, 50, UnidadMedida.UNIDAD, construccion, cementos);

        prod("TOR-001", "Clavo de 3 pulgadas", "Fijaciones GT", "9.50", 150, 30, UnidadMedida.LIBRA, tornilleria, martillo);
        prod("TOR-002", "Tornillo para tablayeso 1\" (caja 100 u.)", "Fijaciones GT", "28.00", 7, 10, UnidadMedida.CAJA, tornilleria, martillo);

        log.info("⚡ ThunderMax: datos de ejemplo cargados ({} productos).", productoRepository.count());
    }

    private Categoria cat(String nombre, String descripcion) {
        return categoriaRepository.save(Categoria.builder().nombre(nombre).descripcion(descripcion).build());
    }

    private Proveedor prov(String nombre, String nit, String contacto, String tel, String email, String dir) {
        return proveedorRepository.save(Proveedor.builder().nombre(nombre).nit(nit).contacto(contacto)
                .telefono(tel).email(email).direccion(dir).build());
    }

    private void prod(String codigo, String nombre, String marca, String precio, int stock, int minimo,
                      UnidadMedida unidad, Categoria categoria, Proveedor proveedor) {
        Producto p = productoRepository.save(Producto.builder()
                .codigo(codigo).nombre(nombre).marca(marca).precio(new BigDecimal(precio))
                .stock(stock).stockMinimo(minimo).unidad(unidad)
                .categoria(categoria).proveedor(proveedor).build());
        kardexRepository.save(MovimientoKardex.builder()
                .fecha(LocalDateTime.now()).producto(p).tipo(TipoMovimiento.INVENTARIO_INICIAL)
                .cantidad(stock).stockAnterior(0).stockNuevo(stock).referencia("Carga inicial").build());
    }
}
