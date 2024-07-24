#!/bin/sh
# Check if DOCKER_ACCOUNT is set, otherwise use default
if [ -z "$DOCKER_ACCOUNT" ]; then
    DOCKER_ACCOUNT=clytze
fi;

# Create a new buildx builder instance and use it
docker buildx create --name mymultiarchbuilder --use
docker buildx inspect --bootstrap

# Build and push images for multiple architectures
docker buildx build --platform linux/amd64,linux/arm64 --tag $DOCKER_ACCOUNT/microservice-kubernetes-demo-apache:latest --push ./apache
docker buildx build --platform linux/amd64,linux/arm64 --tag $DOCKER_ACCOUNT/microservice-kubernetes-demo-catalog-rest:latest --push ./microservice-kubernetes-demo-catalog
docker buildx build --platform linux/amd64,linux/arm64 --tag $DOCKER_ACCOUNT/microservice-kubernetes-demo-customer-rest:latest --push ./microservice-kubernetes-demo-customer
docker buildx build --platform linux/amd64,linux/arm64 --tag $DOCKER_ACCOUNT/microservice-kubernetes-demo-order-rest:latest --push ./microservice-kubernetes-demo-order

# Remove the buildx builder instance when done
docker buildx rm mymultiarchbuilder
