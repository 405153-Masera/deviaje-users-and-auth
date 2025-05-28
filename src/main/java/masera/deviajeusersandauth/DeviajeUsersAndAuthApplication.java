package masera.deviajeusersandauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Aplicación principal para el módulo de usuarios y autenticación de Deviaje.
 * Habilita la ejecución asíncrona para mejorar el rendimiento en operaciones que lo requieran.
 */
@SpringBootApplication
@EnableAsync
public class DeviajeUsersAndAuthApplication {

  /**
  * Metodo principal que inicia la aplicación.*
  *
  * @param args Argumentos de línea de comandos (no utilizados).
  */
  public static void main(String[] args) {
    SpringApplication.run(DeviajeUsersAndAuthApplication.class, args);
  }

}
