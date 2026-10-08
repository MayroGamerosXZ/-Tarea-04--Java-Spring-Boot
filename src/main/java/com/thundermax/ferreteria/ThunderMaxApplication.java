package com.thundermax.ferreteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * ⚡🔩 ThunderMax - Sistema de Ferretería.
 * <p>
 * Punto de entrada. {@code @SpringBootApplication} activa:
 * <ul>
 *   <li>Autoconfiguración (servidor Tomcat, conexión a BD, JSON...)</li>
 *   <li>Escaneo de componentes: encuentra todas las clases con @RestController, @Service, @Repository</li>
 * </ul>
 * Es el equivalente a {@code app.listen(8080)} en Express.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class ThunderMaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThunderMaxApplication.class, args);
	}

}
