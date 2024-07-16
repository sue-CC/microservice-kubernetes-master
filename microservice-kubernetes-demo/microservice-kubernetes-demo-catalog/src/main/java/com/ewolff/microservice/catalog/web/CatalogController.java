package com.ewolff.microservice.catalog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;

import java.util.List;

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
	// delete item
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) {
		if (!itemRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		itemRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}


//package com.ewolff.microservice.catalog.web;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.ewolff.microservice.catalog.Item;
//import com.ewolff.microservice.catalog.ItemRepository;
//
//@Controller
////@RequestMapping("/catalog")
//public class CatalogController {
//
//	private final ItemRepository itemRepository;
//
//	@Autowired
//	public CatalogController(ItemRepository itemRepository) {
//		this.itemRepository = itemRepository;
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView Item(@PathVariable("id") long id) {
//		return new ModelAndView("item", "item", itemRepository.findById(id).get());
//	}
//
//	@RequestMapping("/list.html")
//	public ModelAndView ItemList() {
//
//		return new ModelAndView("itemlist", "items", itemRepository.findAll());
//	}
//
//	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
//	public ModelAndView add() {
//
//		return new ModelAndView("item", "item", new Item());
//	}
//
//	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
//	public ModelAndView post(Item Item) {
//		Item = itemRepository.save(Item);
//		return new ModelAndView("success");
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
//	public ModelAndView put(@PathVariable("id") long id, Item item) {
//		item.setId(id);
//		itemRepository.save(item);
//		return new ModelAndView("success");
//	}
//
//	@RequestMapping(value = "/searchForm.html", produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView searchForm() {
//
//		return new ModelAndView("searchForm");
//	}
//
//	@RequestMapping(value = "/searchByName.html", produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView search(@RequestParam("query") String query) {
//		return new ModelAndView("itemlist", "items",
//				itemRepository.findByNameContaining(query));
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
//	public ModelAndView delete(@PathVariable("id") long id) {
//		itemRepository.deleteById(id);
//		return new ModelAndView("success");
//	}
//
//}
