package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Slf4j
@Configuration
public class Routes {

	@Value("http://localhost:8084")
	private String productServiceUrl;

	@Value("http://localhost:8085")
	private String orderServiceUrl;

	@Value("http://localhost:8086")
	private String inventoryServiceUrl;

	@Bean
	public RouterFunction<ServerResponse> productServiceRoutes(){


		log.info("Initializing product service routes with URL: {}", productServiceUrl);

		return GatewayRouterFunctions.route("product-service")

				.route(RequestPredicates.path("/api/product"), request -> {


					log.info("Received request for product-service: {}", request.uri());

					try{

						ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
						log.info("Response status: {}", response.statusCode());
						return response;

					} catch (Exception e) {

						log.error("Error occurred while routing to: {}", e.getMessage(), e);
						return ServerResponse.status(500).body("Error occurred when routing to product-service");
					}
				})
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> orderServiceRoutes(){
		log.info("Initializing order service routes with URL: {}", orderServiceUrl);

		return GatewayRouterFunctions.route("order-service")

				.route(RequestPredicates.path("/api/order"), request -> {

					log.info("Received request for order-service: {}", request.uri());

					try {

						ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
						log.info("Received response: {}", response.statusCode());
						return response;

					} catch (Exception e) {

						log.error("Error occurred while routing to: {}", e.getMessage(), e);
						return ServerResponse.status(500).body("Error occurred when routing to order-service");

					}
				})
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> inventoryServiceRoutes(){
		log.info("Initializing inventory service routes with URL: {}", inventoryServiceUrl);

		return GatewayRouterFunctions.route("inventory-service")
				.route(RequestPredicates.path("/api/inventory"), request -> {
					log.info("Received request for inventory-service: {}", request.uri());

					try {
						ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
						log.info("Received response: {}", response.statusCode());
						return response;

					} catch (Exception e) {
						log.error("Error occurred while routing to: {}", e.getMessage(), e);
						return ServerResponse.status(500).body("Error occurred when routing to inventory-service");
					}
				})
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
		return GatewayRouterFunctions.route("product_service_swagger")
				.route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
						HandlerFunctions.http("/http:/localhost:8084"))
				.filter(setPath("/api-docs"))
				.build();
	}
	@Bean
	public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
		return GatewayRouterFunctions.route("order_service_swagger")
				.route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
						HandlerFunctions.http("http://localhost:8085"))
				.filter(setPath("/api-docs"))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
		return GatewayRouterFunctions.route("inventory_service_swagger")
				.route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
						HandlerFunctions.http("http://localhost:8086"))
				.filter(setPath("/api-docs"))
				.build();
	}
}
