package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.OrderRequestDto;
import com.adham.store_management_system.dto.OrderResponse;
import com.adham.store_management_system.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequestDto dto) {
        return orderService.createOrder(dto);
    }
}
