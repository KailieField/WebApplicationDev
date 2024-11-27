package ca.gbc.productservice.model;


// IMPORTS
import java.math.BigDecimal;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@Document(value="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	private String id;
	private String name;
	private String description;
	private BigDecimal price;


}
