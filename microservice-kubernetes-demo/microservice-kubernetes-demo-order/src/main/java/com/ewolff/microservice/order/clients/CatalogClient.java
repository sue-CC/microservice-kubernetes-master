package com.ewolff.microservice.order.clients;

import com.ewolff.microservice.catalog.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.protobuf.Empty;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class CatalogClient {

	private CatalogServiceGrpc.CatalogServiceBlockingStub catalogService;

//	@Value("${catalog.service.host}")
//	private String catalogServiceHost;
//
//	@Value("${catalog.service.port}")
//	private int catalogServicePort;

	@PostConstruct
	public void init() {
		// create a channel for communication
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
				.usePlaintext()
				.build();

		// get stub
		catalogService = (channel);
	}

	// prepare the parameters
	public double price(long itemId) {
		CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(itemId).build();
		CatalogProto.ItemResponse response = catalogService.getItem(request);
		return response.getPrice();
	}

	public Collection<Item> findAll() {
		Empty request = Empty.newBuilder().build();
		CatalogProto.ItemListResponse response = catalogService.listItems(request);
		return response.getItemsList().stream()
				.map(item -> new Item(item.getItemId(), item.getName(), item.getPrice()))
				.collect(Collectors.toList());
		}

	public Item getOne(long itemId) {
		CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(itemId).build();
		CatalogProto.ItemResponse response = catalogService.getItem(request);
		return new Item(request.getItemId(), response.getName(), response.getPrice());
	}
}







// package com.ewolff.microservice.order.clients;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.hateoas.MediaTypes;
//import org.springframework.hateoas.PagedModel;
//import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class CatalogClient {
//
//	private final Logger log = LoggerFactory.getLogger(CatalogClient.class);
//
//	public static class ItemPagedResources extends PagedModel<Item> {
//
//	}
//
//	private RestTemplate restTemplate;
//	private String catalogServiceHost;
//	private long catalogServicePort;
//
//	@Autowired
//	public CatalogClient(@Value("${catalog.service.host:catalog}") String catalogServiceHost,
//			@Value("${catalog.service.port:8080}") long catalogServicePort) {
//		super();
//		this.restTemplate = getRestTemplate();
//		this.catalogServiceHost = catalogServiceHost;
//		this.catalogServicePort = catalogServicePort;
//	}
//
//	protected RestTemplate getRestTemplate() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		mapper.registerModule(new Jackson2HalModule());
//
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
//		converter.setObjectMapper(mapper);
//
//		return new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
//	}
//
//	public double price(long itemId) {
//		return getOne(itemId).getPrice();
//	}
//
//	public Collection<Item> findAll() {
//		PagedModel<Item> pagedResources = restTemplate.getForObject(catalogURL(), ItemPagedResources.class);
//		return pagedResources.getContent();
//	}
//
//	private String catalogURL() {
//		String url = String.format("http://%s:%s/catalog/", catalogServiceHost, catalogServicePort);
//		log.trace("Catalog: URL {} ", url);
//		return url;
//	}
//
//	public Item getOne(long itemId) {
//		return restTemplate.getForObject(catalogURL() + itemId, Item.class);
//	}
//}
