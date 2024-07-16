#!/bin/sh
if [ -z "$DOCKER_ACCOUNT" ]; then
    DOCKER_ACCOUNT=clytze
fi;
docker build --tag=microservice-kubernetes-demo-apache apache
docker tag microservice-kubernetes-demo-apache $DOCKER_ACCOUNT/microservice-kubernetes-demo-apache:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-apache

docker build --tag=microservice-kubernetes-demo-catalog-grpc microservice-kubernetes-demo-catalog
docker tag microservice-kubernetes-demo-catalog-grpc $DOCKER_ACCOUNT/microservice-kubernetes-demo-catalog-grpc:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-catalog-grpc

docker build --tag=microservice-kubernetes-demo-customer-grpc microservice-kubernetes-demo-customer
docker tag microservice-kubernetes-demo-customer-grpc $DOCKER_ACCOUNT/microservice-kubernetes-demo-customer-grpc:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-customer-grpc

docker build --tag=microservice-kubernetes-demo-order-grpc microservice-kubernetes-demo-order
docker tag microservice-kubernetes-demo-order-grpc $DOCKER_ACCOUNT/microservice-kubernetes-demo-order-grpc:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-order-grpc