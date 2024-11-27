package ca.gbc.productservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ca.gbc.productservice.dto.ProductRequest;
import org.springframework.web.bind.annotation.*;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductServiceImpl;

import java.util.List;



@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductServiceImpl productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {

		ProductResponse createdProduct = productService.createProduct(productRequest);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/product/" + createdProduct.id());

		return ResponseEntity
				.status(HttpStatus.CREATED) // set status to 201
				.headers(headers)
				.contentType(MediaType.APPLICATION_JSON) // set content-type to JSON
				.body(createdProduct); // return the created product in response body
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponse> getProducts() {

		return productService.getAllProducts();

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") String id,
										   @RequestBody ProductRequest productRequest)
	{
		String updateProductId = productService.updateProduct(id, productRequest);

		if (updateProductId == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Product ID: " + id + "NOT FOUND");
		}

		// set the location header attribute
		HttpHeaders header = new HttpHeaders();
		header.add("Location", "/api/product/" + updateProductId);

		return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


}