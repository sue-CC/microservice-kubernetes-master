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

@Service
public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final ItemRepository itemRepository;

    @Autowired
    public CatalogServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void getItem(CatalogProto.ItemRequest request, StreamObserver<CatalogProto.ItemResponse> responseObserver) {
        super.getItem(request, responseObserver);
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
        // get all items
        List<Item> items = findAllItems();

        // new a builder for the response
        CatalogProto.ItemListResponse.Builder responseBuilder = CatalogProto.ItemListResponse.newBuilder();

        // inject information into the builder using addItem

        for (Item item : items) {
            CatalogProto.ItemResponse itemResponse = CatalogProto.ItemResponse.newBuilder()
                    .setItemId(item.getId())
                    .setName(item.getName())
                    .setPrice(item.getPrice())
                    .build();
            responseBuilder.addItems(itemResponse);
        }

        CatalogProto.ItemListResponse itemListResponse = responseBuilder.build();
        responseObserver.onNext(itemListResponse);
        responseObserver.onCompleted();

//            for (Item item : items) {
//            responseBuilder.addItems(CatalogProto.ItemResponse.newBuilder()
//                    .setItemId(item.getId())
//                    .setName(item.getName())
//                    .setPrice(item.getPrice())
//                    .build());
//
//        }
//        responseObserver.onNext(responseBuilder.setIbuild());
//        responseObserver.onCompleted();
    }


    // 实现查找商品信息的方法
    private Item findItemById(long itemId) {
        // 查询数据库或其他数据源
        return itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // 实现查找所有商品的方法
    private List<Item> findAllItems() {
        // 查询数据库或其他数据源
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}

