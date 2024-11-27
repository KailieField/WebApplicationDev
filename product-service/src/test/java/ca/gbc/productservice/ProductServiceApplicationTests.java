package ca.gbc.productservice;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;


// Tells SpringBoot to look for a main configuration class (@SpringBootApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceApplicationTests.class);

	// automatically configuring the connection to the test MongoDbContainer
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
			.withExposedPorts(27017);

	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
		log.info("Base URI: {}", RestAssured.baseURI);
	}
	static {

		mongoDBContainer.start();

	}

	@Test
	void createProductTest(){

		String requestBody = """
				
				{
						"name": "Hot Water Bottle",
						"description": "Hot Water Bottle - 2024",
						"price": 100
				
				}
				
				
				""";

		//BDD - 0 Behavioural Driven Development (Give, When, Then)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product") // perform the POST request to the /api/product endpoint
				.then()
				.statusCode(201)
				.body("name", Matchers.equalTo("Hot Water Bottle"))
				.body("description", Matchers.equalTo("Hot Water Bottle - 2024"))
				.body("price", Matchers.equalTo(100));
	}

	@Test
	void getAllProductsTest(){

		String requestBody = """
				
				{
					"name": "Hot Water Bottle",
					"description": "Hot Water Bottle - 2024",
					"price": 100
									
				}
				
				
				""";

		// BDD - 0 Behaviour Driven Development (Given, When, Then)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Hot Water Bottle"))
				.body("description", Matchers.equalTo("Hot Water Bottle - 2024"))
				.body("price", Matchers.equalTo(100));

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()", Matchers.greaterThan(0))
				.body("[0].name", Matchers.equalTo("Hot Water Bottle"))
				.body("[0].description", Matchers.equalTo("Hot Water Bottle - 2024"))
				.body("[0].price", Matchers.equalTo(100));
	}




}