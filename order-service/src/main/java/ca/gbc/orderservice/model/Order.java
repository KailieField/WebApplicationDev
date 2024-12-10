
package ca.gbc.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@SuppressWarnings("ALL")
@Entity
@Table(name="t_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="order_number") // adding for successful postman test
	private String orderNumber;

	@Column(name="skuCode")
	private String skuCode;

	@Column(name="price")
	private BigDecimal price;

	@Column(name="quantity")
	private Integer quantity;
}
