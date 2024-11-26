package ca.gbc.orderservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.hamcrest.MatcherAssert;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class OrderServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson= """
			{
				"scuCode": "healing_potion",
				"price": 200,
				"quantity": 50,
			}
			""";

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.when()
				.post("/api/orders")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		MatcherAssert.assertThat(responseBodyString, Matchers.is("Order was placed successfully."));
	}

}