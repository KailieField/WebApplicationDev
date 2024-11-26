package ca.gbc.inventoryservice.controller;


import ca.gbc.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;


	//http://localhost:8086/api/inventory
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity){

		return inventoryService.isInStock(skuCode, quantity);
	}
}
