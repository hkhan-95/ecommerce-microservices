# End-to-end smoke test checklist

Run this checklist from the repository root after all infrastructure and applications are started.

## Platform

- [ ] Config Server returns HTTP 200 for `http://localhost:8888/product-service/default`.
- [ ] Config Server returns HTTP 200 for `http://localhost:8888/order-service/default`.
- [ ] Config Server returns HTTP 200 for `http://localhost:8888/order-activity-service/default`.
- [ ] Eureka shows `PRODUCT-SERVICE`, `ORDER-SERVICE`, and `API-GATEWAY` as `UP` at `http://localhost:8761/eureka/apps`.
- [ ] Gateway actuator shows only the `product-service` and `order-service` routes at `http://localhost:8222/actuator/gateway/routes`.

## Product API

- [ ] `POST /api/v1/products` through port 8222 returns HTTP 201.
- [ ] The response contains a generated `id` and status `AVAILABLE`.
- [ ] `GET /api/v1/products/{id}` through port 8222 returns HTTP 200 and the created product.

## Order API and Feign

- [ ] `POST /api/v1/orders` through port 8222 returns HTTP 201 for an `AVAILABLE` product.
- [ ] The response status is `PLACED`.
- [ ] `totalAmount` matches the referenced product price.
- [ ] `GET /api/v1/orders/{id}` through port 8222 returns the created order.
- [ ] An unknown product returns HTTP 404.
- [ ] An `OUT_OF_STOCK` or `DISCONTINUED` product returns HTTP 409.

## Kafka and activity storage

- [ ] Order Service logs a successful publication to `order-events`.
- [ ] Order Activity Service logs receipt of the same order ID.
- [ ] Kafka group `order-activity-service` reaches lag 0.
- [ ] DynamoDB table `order-activity` contains an item for the order ID.
- [ ] The item has event type `ORDER_CREATED`.
- [ ] The item uses `occurredAt#eventId` as `eventKey`.

## Final checks

- [ ] No application reports deserialization, Feign, database, or routing errors.
- [ ] `./mvnw clean verify` succeeds in every module.

