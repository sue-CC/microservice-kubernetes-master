package com.ewolff.microservice.catalog.grpc.server;

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;
import com.ewolff.microservice.catalog.grpc.*;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);
    private final ItemRepository itemRepository;
    @Autowired
    public CatalogServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void getItem(CatalogProto.ItemRequest request, StreamObserver<CatalogProto.ItemResponse> responseObserver) {
        super.getItem(request, responseObserver);
        logger.info("Received getItem request for itemId: {}", request.getItemId());
        // get the itemId through request
        long itemId = request.getItemId();

        // get the item information through itemId
        Item item = findItemById(itemId);

        // construct a new builder to encapsulate for response
        // inject related information into the builder
        CatalogProto.ItemResponse response = CatalogProto.ItemResponse.newBuilder()
                .setItemId(item.getId())
                .setName(item.getName())
                .setPrice(item.getPrice())
                .build();

        // return the encapsulated response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listItems(Empty request, StreamObserver<CatalogProto.ItemListResponse> responseObserver) {
        super.listItems(request, responseObserver);
        logger.info("Received listItems request");
        // get all items
        List<Item> items = findAllItems();

        // new a builder for the response
        CatalogProto.ItemListResponse.Builder responseBuilder = CatalogProto.ItemListResponse.newBuilder();

        // inject information into the builder using addItems
        for (Item item : items) {
            CatalogProto.Item itemResponse = CatalogProto.Item.newBuilder()
                    .setItemId(item.getId())
                    .setName(item.getName())
                    .setPrice(item.getPrice())
                    .build();
            responseBuilder.addItems(itemResponse); // add each Item to the ItemListResponse
        }

        CatalogProto.ItemListResponse itemListResponse = responseBuilder.build();
        responseObserver.onNext(itemListResponse);
        responseObserver.onCompleted();
    }

    // implement findItemById
    private Item findItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // implement findAllItems
    public List<Item> findAllItems() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}

