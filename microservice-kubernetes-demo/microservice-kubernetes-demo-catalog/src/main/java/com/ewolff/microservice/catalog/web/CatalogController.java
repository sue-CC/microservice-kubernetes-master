package com.ewolff.microservice.catalog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;

@RestController
@RequestMapping("/catalog") // Base URL for all requests in this controller
public class CatalogController {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	// get item
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> getItem(@PathVariable("id") long id) {
		return itemRepository.findById(id)
				.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	// get item list
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Item>> getItemList() {
		Iterable<Item> items = itemRepository.findAll();
		return new ResponseEntity<>(items, HttpStatus.OK);
	}
	// create item
	@PostMapping(value = "/form", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> postItem(@RequestBody Item item) {
		Item savedItem = itemRepository.save(item);
		return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
	}
	// update item
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> putItem(@PathVariable("id") long id, @RequestBody Item item) {
		if (!itemRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		item.setId(id);
		Item updatedItem = itemRepository.save(item);
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteItem(@PathVariable("id") long id) {
		if (!itemRepository.existsById(id)) {
			return new ResponseEntity<>("Item with ID " + id + " not found.", HttpStatus.NOT_FOUND);
		}
		itemRepository.deleteById(id);
		return new ResponseEntity<>("Item deleted successfully!", HttpStatus.NO_CONTENT);
	}
}
