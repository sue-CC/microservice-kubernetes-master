package com.ewolff.microservice.catalog.grpc.server;

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;
import com.ewolff.microservice.catalog.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public void getItem(CatalogProto.ItemRequest request, StreamObserver<CatalogProto.Item> responseObserver) {
        logger.info("Received getItem request for itemId: {}", request.getItemId());
        // get the itemId through request
        long itemId = request.getItemId();

        // get the item information through itemId
        Item item = findItemById(itemId);

        // construct a new builder to encapsulate for response
        // inject related information into the builder
        CatalogProto.Item response = CatalogProto.Item.newBuilder()
                .setItemId(item.getId())
                .setName(item.getName())
                .setPrice(item.getPrice())
                .build();

        // return the encapsulated response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getItemList(Empty request, StreamObserver<CatalogProto.ItemListResponse> responseObserver) {

        // get all items
        Collection<Item> items = findAllItems();
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
            System.out.println("Received listItems request with the order information:\n" + itemListResponse.getItems(1));
            responseObserver.onCompleted();
        }


    @Override
    public void addItem(CatalogProto.AddItemRequest request, StreamObserver<CatalogProto.Item> responseObserver) {
        try {
            Item item = new Item();
            item.setName(request.getName());
            item.setPrice(request.getPrice());

            itemRepository.save(item);

            if (item.getId() == null) {
                throw new RuntimeException("Failed to generate ID for item");
            }

            CatalogProto.Item response = CatalogProto.Item.newBuilder()
                    .setItemId(item.getId())
                    .setName(item.getName())
                    .setPrice(item.getPrice())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            logger.info("Added Item: ID=" + item.getId() + ", Name=" + item.getName() + ", Price=" + item.getPrice());
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }



    @Override
    public void updateItem(CatalogProto.UpdateItemRequest request, StreamObserver<CatalogProto.Item> responseObserver) {
        CatalogProto.Item.Builder itemResponse = CatalogProto.Item.newBuilder();
        Long id = request.getItemId();
        if (!itemRepository.existsById(id)) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Item with ID " + id + " not found.")
                    .asRuntimeException());
        } else {
            Item item = itemRepository.findById(id).get();
            item.setName(request.getName());
            item.setPrice(request.getPrice());
            itemRepository.save(item);

            itemResponse.setItemId(item.getId());
            itemResponse.setName(item.getName());
            itemResponse.setPrice(item.getPrice());

            responseObserver.onNext(itemResponse.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void searchItems(CatalogProto.SearchRequest request, StreamObserver<CatalogProto.ItemListResponse> responseObserver) {
        String query = request.getQuery();
        Collection<Item> items = itemRepository.findByNameContaining(query);
        System.out.println("Found " + items.size() + " items");
        CatalogProto.ItemListResponse.Builder responseBuilder = CatalogProto.ItemListResponse.newBuilder();
        for (Item item : items) {
            CatalogProto.Item itemResponse = CatalogProto.Item.newBuilder()
                    .setItemId(item.getId())
                    .setName(item.getName())
                    .setPrice(item.getPrice())
                    .build();
            responseBuilder.addItems(itemResponse);
        }
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }

    @Override
    public void deleteItem(CatalogProto.ItemRequest request, StreamObserver<CatalogProto.SuccessResponse> responseObserver) {
        Long id = request.getItemId();
        if (!itemRepository.existsById(id)) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Item with ID " + id + " not found.")
                    .asRuntimeException());
        } else {
            itemRepository.deleteById(id);
            CatalogProto.SuccessResponse successResponse = CatalogProto.SuccessResponse.newBuilder().setSuccessMessage("Item deleted successfully!").build();
            responseObserver.onNext(successResponse);
            responseObserver.onCompleted();
        }
    }

    // implement findItemById
    private Item findItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // implement findAllItems
    public Collection<Item> findAllItems() {
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }

}
