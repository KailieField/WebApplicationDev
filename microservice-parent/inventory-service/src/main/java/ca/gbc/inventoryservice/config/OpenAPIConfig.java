package ca.gbc.inventoryservice.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	@Value("v1.0")
	private String version;

	@Bean
	public OpenAPI inventoryServiceOpenAPI() {

		return new OpenAPI()
				.info(new Info().title("Inventory Service API")
					.version(version)
					.license(new License().name("Apache 2.0")))
				.externalDocs(new ExternalDocumentation()
					.description("Inventory Service Wiki Documentation")
					.url("http://mycompany.ca/inventory-service/docs"));
	}
}
