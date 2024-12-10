
package ca.gbc.inventoryservice;


import io.restassured.RestAssured;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.io.IOException;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("inventory-service-test")
			.withUsername("admin")
			.withPassword("password");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {

		postgreSQLContainer.start();
	}


	///  -- TESTS --

	@Test
	void isInStockTest() throws IOException, InterruptedException {

		String insertQuery = """
				
				INSERT INTO t_inventory (sku_code, quantity)
				VALUES ('SKU001, 100);

				""";
		postgreSQLContainer.execInContainer(
				"psql",
				"-U",
				"admin",
				"-d",
				"inventory-service-test",
				"-c",
				insertQuery
		);

		// -- BDD STYLE TEST FOR isInStock ENDPOINT --
		RestAssured.given()
				.contentType("application/json")
				.queryParam("sku_code", "SKU001")
				.queryParam("quantity", 50)
				.when()
				.get("/api/inventory")
				.then()
				.log().all()
				.statusCode(200)
				// - BOOLEAN RESPONSE --
				.body(Matchers.equalTo("true"));
	}

	@Test
	void isNotInStockTest() throws IOException, InterruptedException {

		String insertQuery = """
				
				INSERT INTO t_inventory (sku_code, quantity)
				VALUES ('SKU001, 10);
				
				
				""";
		postgreSQLContainer.execInContainer(
				"psql",
				"-U",
				"admin",
				"-d",
				"inventory-service-test",
				"-c",
				insertQuery);

		// -- BDD STYLE TEST FOR isInStock ENDPOINT --
		RestAssured.given()
				.contentType("application/json")
				.queryParam("sku_code", "SKU001")
				.queryParam("quantity", 20)
				.when()
				.get("/api/inventory")
				.then()
				.log().all()
				.statusCode(200)
				// - BOOLEAN RESPONSE --
				.body(Matchers.equalTo("false"));


	}

}
