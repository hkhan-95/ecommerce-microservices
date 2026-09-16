package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.entity.Order;

public class OrderMapper {
    private OrderMapper() {
    }

    public static Order toEntity(CreateOrderRequest request) {
        Order order = new Order();

        order.setProductId(request.productId());
        order.setCustomerId(request.customerId());
        order.setOrderNotes(request.orderNotes());

        return order;
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getCustomerId(),
                order.getOrderNotes(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
