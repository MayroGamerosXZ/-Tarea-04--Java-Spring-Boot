# ⚡ ThunderMax - Sistema de Ferretería
**API REST desarrollada con Spring Boot y PostgreSQL**

## 1. Funcionalidad del Sistema

ThunderMax no es solo un CRUD básico; incluye lógica de negocio real diseñada para una ferretería:

* **Gestión de Catálogos:** Clientes (con validación de NIT/CF), Proveedores, Categorías y Productos con unidades de medida reales (metros, galones, libras).
* **Control de Inventario (Kárdex):** El stock de los productos no se edita a mano. Se modifica exclusivamente a través de Entradas de Mercadería o Ventas. Cada movimiento se registra en una bitácora (kárdex) detallando el stock anterior y el nuevo.
* **Alertas de Reabastecimiento:** Sistema que detecta automáticamente qué productos están por debajo de su límite de `stockMinimo`.
* **Ventas y Anulaciones:** Calcula el IVA (12%), valida existencias antes de vender, descuenta el inventario y permite anular ventas (devolviendo el stock a bodega).
* **Cotizaciones:** Permite crear proformas con vigencia que luego se pueden convertir en ventas con un solo clic.
* **Calculadora de Materiales:** Algoritmos que, dados los metros cuadrados, calculan cuántos galones de pintura o bolsas de cemento se necesitan, y *sugieren* productos disponibles en stock.
* **Reportes:** Resumen de ingresos diarios, top de productos más vendidos y cálculo del valor total del inventario en quetzales.

---

## 2. Reflexiones sobre el Desarrollo

*(Nota: Lee esta sección en el video con tus propias palabras)*

### ¿Qué aprendí o reforcé?
Con este proyecto reforcé mis conocimientos sobre la arquitectura backend. Viniendo de un entorno como Node.js (donde hay que configurar muchas cosas a mano), con Spring Boot y Java aprendí el uso de **anotaciones** (como `@RestController` o `@Service`), que automatizan la inyección de dependencias. También aprendí a usar **JPA e Hibernate**, que permiten interactuar con PostgreSQL usando objetos de Java en lugar de escribir consultas SQL manualmente.

### ¿Qué fue lo más interesante?
Lo más interesante fue implementar el **Kárdex y las Transacciones (`@Transactional`)**. En un proyecto básico, uno simplemente le suma o resta a la columna `stock` y ya. Pero aquí, cada vez que hay una venta, el sistema guarda un registro histórico (movimiento), actualiza el stock y guarda la factura. Si algo falla a la mitad (por ejemplo, falta stock del tercer producto de la lista), Spring Boot echa para atrás todo el proceso de golpe (rollback) para que la base de datos no quede inconsistente.

### ¿Qué no sabía cómo funcionaba y ahora tengo bien claro?
Antes no entendía bien cómo funcionaba **Swagger (OpenAPI)**. Yo pensaba que uno tenía que diseñar esa página web a mano para documentar la API. Ahora tengo claro que, agregando una simple dependencia (`springdoc-openapi`) y usando comentarios en los controladores de Java, Spring lee mi código y genera automáticamente toda la interfaz interactiva de prueba, mostrando qué parámetros recibe cada endpoint y qué respuestas da.

---

## 3. Comparativa: Node.js (Express) vs Spring Boot (Java)

| Concepto | En Node.js (Express) | En Spring Boot |
| :--- | :--- | :--- |
| **Punto de arranque** | `app.listen(3000, () => ...)` | `@SpringBootApplication` y método `main()` |
| **Definir una Ruta (API)**| `router.get('/ventas', ...)` | Anotación `@GetMapping("/ventas")` |
| **Manejador de Paquetes** | `npm` (`package.json`) | Maven (`pom.xml`) |
| **ORM (Base de datos)** | Sequelize / Mongoose / Prisma | JPA (Hibernate) mediante `@Entity` |
| **Consultas a BD** | `Producto.findAll()` | `productoRepository.findAll()` |
| **Validaciones** | Librerías como `Joi` o `express-validator` | Anotaciones de Jakarta (`@NotNull`, `@Size`) |
| **Variables de entorno** | Archivo `.env` (con `dotenv`) | Archivo `application.properties` |
| **Manejo de Errores** | Middleware `(err, req, res, next)` | Clase anotada con `@RestControllerAdvice` |

---

## 4. Ejecución del Proyecto

1. Levantar la base de datos: `docker compose up -d`
2. Compilar y correr la aplicación: `.\mvnw.cmd spring-boot:run`
3. Documentación interactiva de la API (Swagger): http://localhost:8080/swagger-ui.html
