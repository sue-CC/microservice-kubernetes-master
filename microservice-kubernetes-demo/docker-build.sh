#!/bin/sh
if [ -z "$DOCKER_ACCOUNT" ]; then
    DOCKER_ACCOUNT=clytze
fi;
docker build --tag=microservice-kubernetes-demo-apache apache
docker tag microservice-kubernetes-demo-apache $DOCKER_ACCOUNT/microservice-kubernetes-demo-apache:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-apache

docker build --tag=microservice-kubernetes-demo-catalog-rest microservice-kubernetes-demo-catalog
docker tag microservice-kubernetes-demo-catalog-rest $DOCKER_ACCOUNT/microservice-kubernetes-demo-catalog-rest:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-catalog-rest

docker build --tag=microservice-kubernetes-demo-customer-rest microservice-kubernetes-demo-customer
docker tag microservice-kubernetes-demo-customer-rest $DOCKER_ACCOUNT/microservice-kubernetes-demo-customer-rest:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-customer-rest

docker build --tag=microservice-kubernetes-demo-order-rest microservice-kubernetes-demo-order
docker tag microservice-kubernetes-demo-order-rest $DOCKER_ACCOUNT/microservice-kubernetes-demo-order-rest:latest
docker push $DOCKER_ACCOUNT/microservice-kubernetes-demo-order-rest