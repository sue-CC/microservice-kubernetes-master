package com.ewolff.microservice.order.grpc.client;

import com.ewolff.microservice.order.grpc.OrderProto;
import com.ewolff.microservice.order.grpc.OrderServiceGrpc;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderLine;
import com.ewolff.microservice.order.logic.OrderRepository;
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
public class orderClientImpl implements orderClient {

    private final OrderRepository orderRepository;
    private static final Logger logger = Logger.getLogger(orderClientImpl.class.getName());
    private final OrderServiceGrpc.OrderServiceBlockingStub orderService;
    private final ManagedChannel channel;

    @Autowired// construct client for accessing catalog server using the channel
    public orderClientImpl(@Value("${order.server.host:order-service-grpc}") String host,
                           @Value("${order.server.port:9097}") int port, OrderRepository orderRepository) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.orderService = OrderServiceGrpc.newBlockingStub(channel);
        this.orderRepository = orderRepository;

    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

    @Override
    public Order getOrder(long orderId) {
        OrderProto.OrderId request = OrderProto.OrderId.newBuilder().setId(orderId).build();
        OrderProto.OrderResponse response = orderService.getOrder(request);
        return fromProtoOrder(response.getOrder());
    }

    @Override
    public Collection<Order> getOrders() {
        OrderProto.OrderListResponse response = orderService.listOrders(Empty.newBuilder().build());
        return response.getOrdersList().stream().map(this::fromProtoOrder).collect(Collectors.toList());
    }

    @Override
    public Order createOrder(Order order) {
        OrderProto.CreateOrderRequest.Builder requestBuilder = OrderProto.CreateOrderRequest.newBuilder()
                .setCustomerId(order.getCustomerId());

        for (OrderLine line : order.getOrderLine()) {
            OrderProto.OrderLine orderLineProto = OrderProto.OrderLine.newBuilder()
                    .setCount(line.getCount())
                    .setItemId(line.getItemId())
                    .setNote(line.getNote())
                    .build();
            requestBuilder.addLines(orderLineProto);
        }

        OrderProto.CreateOrderRequest request = requestBuilder.build();
        OrderProto.OrderResponse response = orderService.createOrder(request);
        return fromProtoOrder(response.getOrder());
    }

    @Override
    public String deleteOrder(long orderId) {
        OrderProto.OrderId request = OrderProto.OrderId.newBuilder().setId(orderId).build();
        OrderProto.SuccessResponse response = orderService.deleteOrder(request);
        return response.getSuccessMessage();
    }

    private Order fromProtoOrder(OrderProto.Order protoOrder) {
        Order order = new Order();
        order.setId(protoOrder.getId());
        order.setCustomerId(protoOrder.getCustomerId());
        protoOrder.getLinesList().forEach(line -> order.addLine(line.getCount(), line.getItemId(), line.getNote()));
        return order;
    }
}

