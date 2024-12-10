
package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderRequest;

public interface OrderService {

	default void placeOrder(OrderRequest orderRequest) {

	}
}
