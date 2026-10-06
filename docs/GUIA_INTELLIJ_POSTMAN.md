# Guía de Ejecución: IntelliJ IDEA y Postman

Esta guía es para que tú la sigas paso a paso. No necesitas a tu compañero, yo te explico cómo hacerlo en tu equipo.

## 1. Levantar la Base de Datos (Docker)

1. Asegúrate de tener **Docker Desktop** abierto.
2. Abre una terminal (PowerShell o CMD) en la carpeta del proyecto (`Tarea 04`).
3. Ejecuta el comando:
   ```bash
   docker compose up -d
   ```
   *Esto levanta PostgreSQL en el puerto 5433 (para no chocar con tu otro contenedor).*

## 2. Abrir y ejecutar en IntelliJ IDEA Ultimate

1. Abre **IntelliJ IDEA**.
2. Haz clic en **Open** (o File > Open).
3. Selecciona la carpeta `Tarea 04` (la que contiene el `pom.xml`) y dale a OK.
4. **IMPORTANTE:** En la esquina inferior derecha verás una barra de progreso que dice "Resolving dependencies...". **Espera a que termine** (tarda un par de minutos la primera vez).
5. Abre el archivo: `src/main/java/com/thundermax/ferreteria/ThunderMaxApplication.java`.
6. Haz clic en el botón verde de "Play" (▶️) junto a la línea `public class ThunderMaxApplication` y selecciona **Run 'ThunderMaxApplication'**.
7. Abajo, en la consola, verás que Spring Boot arranca y el logotipo de Spring aparece. Cuando veas `Started ThunderMaxApplication...`, la API ya está funcionando.

### 2.1 Ver las tablas en IntelliJ (Pestaña Database)
Como tienes la versión Ultimate, puedes ver la base de datos sin salir del editor:
1. En el borde derecho de IntelliJ, busca la pestaña **Database**.
2. Clic en el `+` > **Data Source** > **PostgreSQL**.
3. Llena los datos así:
   - **User:** `thundermax`
   - **Password:** `thundermax123`
   - **Port:** `5433` (cámbialo, por defecto dice 5432)
   - **Database:** `thundermax_db`
4. Dale a **Test Connection**. Si te pide descargar un driver, dale click al aviso. Luego dale a **OK**.
5. Abre las carpetas: `thundermax_db` > `schemas` > `public` > `tables`. ¡Ahí están tus tablas! Haz doble clic en una para ver los datos de ejemplo (martillos, pintura, etc.).

## 3. Probar la API en el Navegador (Swagger)

Abre Chrome y entra a:
👉 **http://localhost:8080/swagger-ui.html**

Ahí verás la documentación completa, dividida en las 8 secciones que planificamos. Puedes abrir un endpoint, darle clic a **"Try it out"** y luego a **"Execute"** para probarlo directamente en la página, ¡sin Postman!

## 4. Probar en Postman (opcional, si lo piden)

He preparado una colección lista para importar.
1. Abre **Postman**.
2. Arriba a la izquierda haz clic en **Import**.
3. Selecciona el archivo `docs/postman/ThunderMax.postman_collection.json`.
4. Aparecerá a la izquierda la carpeta "ThunderMax - Ferretería". Ábrela y ejecuta las pruebas (por ejemplo, registrar una venta o calcular pintura).
