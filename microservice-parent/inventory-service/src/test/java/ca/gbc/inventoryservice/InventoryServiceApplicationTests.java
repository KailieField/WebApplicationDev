package ca.gbc.inventoryservice;


import ca.gbc.inventoryservice.service.InventoryService;

import org.mockito.Mockito;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.TestcontainersConfiguration;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void testInStock(){

		String skuCode = "skuCode123";
		Integer quantity = 5;

		InventoryService mockInventoryService = Mockito.mock(InventoryService.class);
		when(mockInventoryService.isInStock(skuCode, quantity)).thenReturn(true);

		given()
				.param("skuCode", skuCode)
				.param("quantity", quantity)
				.when()
				.get("/api/inventory")
				.then()
				.statusCode(200)
				.body(is("true"));
	}

}
