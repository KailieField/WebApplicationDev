package ca.gbc.apigateway.routes;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Slf4j
@Configuration
public class Routes {

	@Value("http://localhost:8084/api/product")
	private String productServiceUrl;

	@Value("http://localhost:8085/api/order")
	private String orderServiceUrl;

	@Value("http://localhost:8086/api/inventory")
	private String inventoryServiceUrl;

	@Bean
	public RouterFunction<ServerResponse> productServiceRoute() {


		log.info("Initializing product service routes with URL: {}", productServiceUrl);

		return route("product-service")

				.route(RequestPredicates.path("/api/product"), request -> {


					log.info("Received request for product-service: {}", request.uri());

					return HandlerFunctions.http(productServiceUrl).handle(request);

				})
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}



	@Bean
	public RouterFunction<ServerResponse> orderServiceRoute(){
		log.info("Initializing order service routes with URL: {}", orderServiceUrl);

		return route("order-service")

				.route(RequestPredicates.path("/api/order"), request -> {

					log.info("Received request for order-service: {}", request.uri());

					return HandlerFunctions.http(orderServiceUrl).handle(request);

				})
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> inventoryServiceRoute(){
		log.info("Initializing inventory service routes with URL: {}", inventoryServiceUrl);

		return route("inventory-service")
				.route(RequestPredicates.path("/api/inventory"), request -> {

					log.info("Received request for inventory-service: {}", request.uri());

					return HandlerFunctions.http(inventoryServiceUrl).handle(request);

				})
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("inventoryServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
		return route("product_service_swagger")
				.route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
						HandlerFunctions.http(productServiceUrl))
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("ProductServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}
	@Bean
	public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
		return route("order_service_swagger")
				.route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
						HandlerFunctions.http(orderServiceUrl))
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("OrderServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
		return route("inventory_service_swagger")
				.route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
						HandlerFunctions.http(inventoryServiceUrl))
				.filter(CircuitBreakerFilterFunctions
						.circuitBreaker("InventoryServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> fallbackRoute(){
		return route("fallbackRoute")
				.route(RequestPredicates.all(),
						request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
								.body("Service is temporarily unaavailable. Please try again later."))
				.build();
	}
}
