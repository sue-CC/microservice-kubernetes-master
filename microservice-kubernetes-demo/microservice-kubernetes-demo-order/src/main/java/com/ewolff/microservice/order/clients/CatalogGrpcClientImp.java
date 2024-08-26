package com.ewolff.microservice.order.clients;

import com.ewolff.microservice.catalog.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.protobuf.Empty;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Component
public class CatalogGrpcClientImp implements CatalogClient {
    private static final Logger logger = Logger.getLogger(CatalogGrpcClientImp.class.getName());
    private final CatalogServiceGrpc.CatalogServiceBlockingStub catalogService;

    // construct client for accessing catalog server using the channel
    public CatalogGrpcClientImp(@Value("${catalog.server.host:catalog-service-grpc}") String host,
                                @Value("${catalog.server.port:9095}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.catalogService = CatalogServiceGrpc.newBlockingStub(channel);
    }

    // get information from the channel

    @Override
    public Item getOne(long itemId) {
        CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(itemId).build();
        CatalogProto.Item response = catalogService.getItem(request);
        logger.info("Id:" + response.getItemId()+ " " + "Name:" + response.getName() + " " + "Price" + response.getPrice());
        return new Item(request.getItemId(), response.getName(), response.getPrice());
    }

    @Override
    public Collection<Item> findAll() {
        Empty request = Empty.newBuilder().build();
        CatalogProto.ItemListResponse response = catalogService.getItemList(request);
        System.out.println("Received items.");
        return response.getItemsList().stream()
                .map(item -> new Item(item.getItemId(), item.getName(), item.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public double price(long itemId) {
        CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(itemId).build();
        return catalogService.getItem(request).getPrice();
    }
}