//package com.ewolff.microservice.order.grpc.server;
//
//import com.ewolff.microservice.catalog.Item;
//import com.ewolff.microservice.catalog.ItemRepository;
//import com.ewolff.microservice.order.grpc.CatalogServiceGrpc;
//import com.ewolff.microservice.order.grpc.ItemList;
//import com.google.protobuf.Empty;
//import io.grpc.stub.StreamObserver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CatalogGrpcService extends CatalogServiceGrpc.CatalogServiceImplBase{
//
//    private final ItemRepository itemRepository;
//
//    @Autowired
//    public CatalogGrpcService(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }
//
//    @Override
//    public void getAll(Empty request, StreamObserver<ItemList> responseObserver) {
//        Iterable<Item> items = itemRepository.findAll();
//
//        // 将 Item 转换为 gRPC 的 ItemList
//        ItemList.Builder responseBuilder = ItemList.newBuilder();
//        items.forEach(item -> {
//            com.ewolff.microservice.order.grpc.Item grpcItem = com.ewolff.microservice.order.grpc.Item.newBuilder()
//                    .setItemId(item.getId())
//                    .setName(item.getName())
//                    .setPrice(item.getPrice())
//                    .build();
//
//            // 构造并发送响应
//            ItemList itemList = responseBuilder.build();
//            responseObserver.onNext(itemList);
//            responseObserver.onCompleted();
//        });
//    }
//}