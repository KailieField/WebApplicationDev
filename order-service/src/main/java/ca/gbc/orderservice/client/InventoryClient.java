package ca.gbc.orderservice.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import groovy.util.logging.Slf4j;

@Slf4j
public interface InventoryClient {

	Logger log = LoggerFactory.getLogger(InventoryClient.class);

	@GetExchange("/api/inventroy")
	@CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
	@Retry(name="inventory")
	boolean isInStock(@RequestParam String skucode, @RequestParam Integer quantity);

	default boolean fallbackMethod(String code, Integer quantity, Throwable throwable) {
		log.info("Cannot get inventory for skucode {}, reasoning: {}", code, throwable.getMessage());
		return false;

	}
}