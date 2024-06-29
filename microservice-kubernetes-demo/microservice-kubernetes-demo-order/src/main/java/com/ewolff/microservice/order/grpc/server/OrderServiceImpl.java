package com.ewolff.microservice.order.grpc.server;

import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderLine;
import com.ewolff.microservice.order.logic.OrderRepository;
import com.ewolff.microservice.order.grpc.OrderProto;
import com.ewolff.microservice.order.grpc.OrderServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void listOrders(Empty request, StreamObserver<OrderProto.OrderListResponse> responseObserver) {
        try {
            List<OrderProto.Order> orderProtos = new ArrayList<>();

            // Fetch orders and convert each to OrderProto.Order
            orderRepository.findAll().forEach(order -> {
                OrderProto.Order.Builder orderBuilder = OrderProto.Order.newBuilder()
                        .setId(order.getId())
                        .setCustomerId(order.getCustomerId());

                // Ensure order lines are initialized within transaction
                List<OrderLine> orderLines = new ArrayList<>(order.getOrderLine());

                for (OrderLine line : orderLines) {
                    OrderProto.OrderLine orderLineProto = OrderProto.OrderLine.newBuilder()
                            .setCount(line.getCount())
                            .setItemId(line.getItemId())
                            .build();
                    orderBuilder.addLines(orderLineProto);
                }

                orderProtos.add(orderBuilder.build());
            });

            OrderProto.OrderListResponse response = OrderProto.OrderListResponse.newBuilder()
                    .addAllOrders(orderProtos)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Failed to list orders", e);
            responseObserver.onError(e);
        }
    }


    @Override
    public void getOrder(OrderProto.OrderId request, StreamObserver<OrderProto.OrderResponse> responseObserver) {
        long orderId = request.getId();
        try {
            Order order = findOrderById(orderId);

            OrderProto.Order.Builder orderBuilder = OrderProto.Order.newBuilder()
                    .setId(order.getId())
                    .setCustomerId(order.getCustomerId());

            // Ensure order lines are initialized within transaction
            List<OrderLine> orderLines = new ArrayList<>(order.getOrderLine());

            for (OrderLine line : orderLines) {
                OrderProto.OrderLine orderLineProto = OrderProto.OrderLine.newBuilder()
                        .setCount(line.getCount())
                        .setItemId(line.getItemId())
                        .build();
                orderBuilder.addLines(orderLineProto);
            }

            OrderProto.OrderResponse response = OrderProto.OrderResponse.newBuilder()
                    .setOrder(orderBuilder.build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Failed to get order with ID " + orderId, e);
            responseObserver.onError(e);
        }
    }


    @Override
    public void createOrder(OrderProto.CreateOrderRequest request, StreamObserver<OrderProto.OrderResponse> responseObserver) {
        try {
            Order order = new Order();
            order.setCustomer(request.getCustomerId());

            for (OrderProto.OrderLine line : request.getLinesList()) {
                order.addLine(line.getCount(), line.getItemId());
            }

            // Save the order
            orderRepository.save(order);

            // Build OrderProto.OrderResponse directly
            OrderProto.OrderResponse response = OrderProto.OrderResponse.newBuilder()
                    .setOrder(
                            OrderProto.Order.newBuilder()
                                    .setId(order.getId())
                                    .setCustomerId(order.getCustomerId())
                                    .addAllLines(order.getOrderLine().stream()
                                            .map(line -> OrderProto.OrderLine.newBuilder()
                                                    .setCount(line.getCount())
                                                    .setItemId(line.getItemId())
                                                    .build())
                                            .collect(Collectors.toList())
                                    )
                                    .build()
                    )
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Failed to create order", e);
            responseObserver.onError(e);
        }
    }


    @Override
    public void deleteOrder(OrderProto.OrderId request, StreamObserver<OrderProto.SuccessResponse> responseObserver) {
        long orderId = request.getId();
        try {
            orderRepository.deleteById(orderId);
            OrderProto.SuccessResponse response = OrderProto.SuccessResponse.newBuilder()
                    .setSuccessMessage("Order deleted successfully!")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Failed to delete order with ID " + orderId, e);
            responseObserver.onError(e);
        }
    }

    private Order findOrderById(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Collection<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

//    private OrderProto.Order toProtoOrder(Order order) {
//        OrderProto.Order.Builder orderBuilder = OrderProto.Order.newBuilder()
//                .setId(order.getId())
//                .setCustomerId(order.getCustomerId());
//
//        // Ensure order lines are initialized within transaction
//        List<OrderLine> orderLines = new ArrayList<>(order.getOrderLine());
//
//        for (OrderLine line : orderLines) {
//            OrderProto.OrderLine orderLineProto = OrderProto.OrderLine.newBuilder()
//                    .setCount(line.getCount())
//                    .setItemId(line.getItemId())
//                    .build();
//            orderBuilder.addLines(orderLineProto);
//        }
//
//        return orderBuilder.build();
//    }

}
