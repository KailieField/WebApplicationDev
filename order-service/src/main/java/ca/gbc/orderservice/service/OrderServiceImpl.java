
package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.event.OrderPlacedEvent;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.respository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;

	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	@Override
	public void placeOrder(OrderRequest orderRequest) {

		var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

		if (isProductInStock) {
			Order order = Order.builder()
					.orderNumber(UUID.randomUUID().toString())
					.price(orderRequest.price())
					.skuCode(orderRequest.skuCode())
					.quantity(orderRequest.quantity())
					.build();

			orderRepository.save(order);


			// -- SEND MESSAGE TO KAFKA TOPIC --
			OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
			orderPlacedEvent.setOrderNumber(order.getOrderNumber());
			orderPlacedEvent.setEmail(orderRequest.userDetails().email());
			orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
			orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

			log.info("Schema: {}", orderPlacedEvent.getSchema());
			log.info("Start -- Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
			kafkaTemplate.send("order-placed", orderPlacedEvent);
			log.info("End -- Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);

		} else {
			throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + "is not in stock");

		}
	}
}




