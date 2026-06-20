package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.OrderItemRequestDto;
import com.adham.store_management_system.dto.OrderRequestDto;
import com.adham.store_management_system.dto.OrderResponse;
import com.adham.store_management_system.entity.Order;
import com.adham.store_management_system.entity.OrderItem;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.mapper.OrderMapper;
import com.adham.store_management_system.repository.OrderRepository;
import com.adham.store_management_system.repository.ProductRepository;
import com.adham.store_management_system.user.User;
import com.adham.store_management_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "products::item", allEntries = true)
    public OrderResponse createOrder(OrderRequestDto requestDto){
        // get user who created order
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication != null ? authentication.getName() : null;
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(()
                -> new ResourceNotFoundException("User not found"));
        log.info("Creating order for user: {}", currentUserEmail);
        Order order = new Order();
        order.setLocalDateTime(LocalDateTime.now());
        order.setUser(user);

        BigDecimal accumulatedTotal = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : requestDto.getItems()){
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> {
                        log.warn("Product not found while creating order. productId: {}", itemDto.getProductId());
                        return new ResourceNotFoundException("Product not found");
                    });
            log.debug("Processing order item. productId: {}, requestedQuantity: {}, availableStock: {}",
                    itemDto.getProductId(), itemDto.getQuantity(), product.getStockQuantity());

            if (product.getStockQuantity() < itemDto.getQuantity()) {
                log.warn("Insufficient stock for product: {} .Available: {}, Requested: {}",
                        product.getName(),product.getStockQuantity(),itemDto.getQuantity());
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName()
                        + ". Available: " + product.getStockQuantity() + ", Requested: " + itemDto.getQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - itemDto.getQuantity());
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            accumulatedTotal=accumulatedTotal.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .priceAtSale(product.getPrice())
                    .quantity(itemDto.getQuantity())
                    .build();

            order.addOrderItem(orderItem);
        }
        order.setTotalPrice(accumulatedTotal);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully. orderId: {}, totalPrice: {}", savedOrder.getId(), savedOrder.getTotalPrice());

        return OrderMapper.toResponse(savedOrder);
    }

}
