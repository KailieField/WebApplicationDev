package ca.gbc.productservice.config;


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
	public OpenAPI productServiceAPI() {

		return new OpenAPI()
				.info(new Info().title("Product Service API")
						.description("This is the Product Service API")
						.version(version)
						.license(new License().name("Apache 2.0")))
				.externalDocs(new ExternalDocumentation()
						.description("Product Service Wiki Documentation")
						.url("http://mycompany.ca/product-service/docs"));
	}
}