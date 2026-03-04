package com.tfg.API_TFG;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * @auth Alejandro del Valle Vallés, Aurora Pinar del Hoyo, Carolina Lobato Ruiz
 */
public class ApiTfgApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ApiTfgApplication.class, args);
	}

}
