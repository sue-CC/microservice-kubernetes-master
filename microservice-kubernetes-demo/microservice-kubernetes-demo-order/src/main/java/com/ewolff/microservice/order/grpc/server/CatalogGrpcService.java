package com.ewolff.microservice.order.grpc.server;

import com.ewolff.microservice.order.grpc.CatalogServiceGrpc;
import com.ewolff.microservice.order.grpc.ItemList;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class CatalogGrpcService extends CatalogServiceGrpc.CatalogServiceImplBase{

    @Override
    public void getAll(Empty request, StreamObserver<ItemList> responseObserver) {
        super.getAll(request, responseObserver);
    }
}