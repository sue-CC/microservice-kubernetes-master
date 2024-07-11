package com.ewolff.microservice.catalog.grpc.client;

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;
import com.ewolff.microservice.catalog.grpc.CatalogProto;
import com.ewolff.microservice.catalog.grpc.CatalogServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class catalogClientImpl implements catalogClient {

    private final ItemRepository itemRepository;
    private static final Logger logger = Logger.getLogger(catalogClientImpl.class.getName());
    private final CatalogServiceGrpc.CatalogServiceBlockingStub catalogService;
    private final ManagedChannel channel;

    @Autowired// construct client for accessing catalog server using the channel
    public catalogClientImpl(@Value("${catalog.service.host:localhost}") String host,
                                @Value("${catalog.service.port:9091}") int port, ItemRepository itemRepository) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.catalogService = CatalogServiceGrpc.newBlockingStub(channel);
        this.itemRepository = itemRepository;
    }

    @PreDestroy
        public void shutdown() {
            if (channel != null) {
                channel.shutdown();
            }
        }

    @Override
    public Item getItem(Long id) {
        CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(id).build();
        if (itemRepository.existsById(id)) {
            CatalogProto.Item response = catalogService.getItem(request);
            return new Item(response.getName(), response.getPrice());
        }
        else return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Collection<Item> getItems() {
        Empty request= Empty.newBuilder().build();
        CatalogProto.ItemListResponse response = catalogService.getItemList(request);
//        System.out.println("Received items.");
        return response.getItemsList().stream()
                .map(item -> new Item(item.getName(), item.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item) {
        CatalogProto.AddItemRequest request = CatalogProto.AddItemRequest.newBuilder()
                                            .setName(item.getName())
                                            .setPrice(item.getPrice())
                                            .build();
        CatalogProto.Item response = catalogService.addItem(request);
        itemRepository.save(item);
        return new Item(response.getName(), response.getPrice());
    }

    @Override
    public Item updateItem(Long id, String name, double price) {
        Item item = itemRepository.findById(id).get();
             item.setName(name);
             item.setPrice(price);
        CatalogProto.UpdateItemRequest request = CatalogProto.UpdateItemRequest.newBuilder()
                .setItemId(id)
                .setName(name)
                .setPrice(price)
                .build();
        itemRepository.save(item);
        CatalogProto.Item response = catalogService.updateItem(request);
        return new Item(response.getName(), response.getPrice());
    }

    @Override
    public String deleteItem(Long id) {
        CatalogProto.ItemRequest request = CatalogProto.ItemRequest.newBuilder().setItemId(id).build();
        CatalogProto.SuccessResponse response = catalogService.deleteItem(request);
        return response.getSuccessMessage();
    }

    @Override
    public Collection<Item> searchItems(String query) {
       CatalogProto.SearchRequest request = CatalogProto.SearchRequest.newBuilder().setQuery(query).build();
       CatalogProto.ItemListResponse response = catalogService.searchItems(request);
        return response.getItemsList().stream()
                .map(item -> new Item(item.getName(), item.getPrice()))
                .collect(Collectors.toList());
    }
}
