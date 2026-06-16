package com.adham.store_management_system.mapper;

import com.adham.store_management_system.dto.OrderItemResponse;
import com.adham.store_management_system.dto.OrderResponse;
import com.adham.store_management_system.entity.Order;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toResponse(Order order){
        if (order == null) return null;

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .dateTime(order.getLocalDateTime())
                .createdBy(order.getUser().getName())
                .items(order.getOrderItems().stream().map(item ->
                        OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .priceAtSale(item.getPriceAtSale())
                                .quantity(item.getQuantity())
                                .build()).collect(Collectors.toList()))
                .build();

    }
}
