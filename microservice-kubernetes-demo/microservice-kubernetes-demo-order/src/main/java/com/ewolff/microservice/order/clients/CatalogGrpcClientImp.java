package com.ewolff.microservice.order.clients;

import com.ewolff.microservice.catalog.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.protobuf.Empty;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Component
public class CatalogGrpcClientImp implements CatalogClient {
    private static final Logger logger = Logger.getLogger(CatalogGrpcClientImp.class.getName());
    private final CatalogServiceGrpc.CatalogServiceBlockingStub catalogService;

    // construct client for accessing catalog server using the channel
    public CatalogGrpcClientImp(@Value("${customer.service.host:localhost}") String host,
                                @Value("${customer.service.port:9091}") int port) {
//        String url = String.format("http://%s:%d", host, port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.catalogService = CatalogServiceGrpc.newBlockingStub(channel);
    }

    // get information from the channel

    @Override
    public Item getOne(long itemId) {
        CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(itemId).build();
        CatalogProto.ItemResponse response = catalogService.getItem(request);
        logger.info("Id:" + response.getItemId()+ " " + "Name:" + response.getName() + " " + "Price" + response.getPrice());
        return new Item(request.getItemId(), response.getName(), response.getPrice());
    }

    @Override
    public Collection<Item> findAll() {
        Empty request = Empty.newBuilder().build();
        CatalogProto.ItemListResponse response = catalogService.listItems(request);
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
