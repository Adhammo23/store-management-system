package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.OrderItemRequestDto;
import com.adham.store_management_system.dto.OrderRequestDto;
import com.adham.store_management_system.dto.OrderResponse;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Order;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.repository.OrderRepository;
import com.adham.store_management_system.repository.ProductRepository;
import com.adham.store_management_system.user.User;
import com.adham.store_management_system.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private OrderService orderService;

    private User user;

    @BeforeEach
    void setup(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        user = User.builder()
                .id(1L)
                .email("test@email.com")
                .name("Adham")
                .build();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
    }

    @Test
    void createOrder_whenValidInput_shouldReturnOrderResponse() {

        // Arrange Security >> setup();

        // Arrange - Category + Product
        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .stockQuantity(100)
                .category(category)
                .build();

        // Arrange - Request

        OrderItemRequestDto itemRequestDto = OrderItemRequestDto.builder()
                .productId(1L)
                .quantity(5)
                .build();

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .items(List.of(itemRequestDto))
                .build();

        // Arrange - Mocks
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderResponse response = orderService.createOrder(orderRequestDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTotalPrice()).isEqualByComparingTo("4999.95");
        assertThat(response.getCreatedBy()).isEqualTo("Adham");

        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test
    void createOrder_whenProductNotFound_shouldThrowResourceNotFoundException (){

//        // Arrange Security >> setup();

        // Arrange Request
        OrderItemRequestDto orderItemRequestDto = OrderItemRequestDto.builder()
                .productId(1L)
                .quantity(5)
                .build();

        OrderRequestDto requestDto = OrderRequestDto.builder()
                .items(List.of(orderItemRequestDto))
                .build();
        // Arrange Mocks

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // Assert + Act
        assertThrows(ResourceNotFoundException.class,()-> orderService.createOrder(requestDto));

        verify(orderRepository,never()).save(any());
    }

    @Test
    void createOrder_whenInsufficientStock_shouldThrowIllegalArgumentException () {

        // Arrange Security >> setup();

        // Arrange Product + Category

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .stockQuantity(10)
                .category(category)
                .build();

        // Arrange - Request

        OrderItemRequestDto itemRequestDto = OrderItemRequestDto.builder()
                .productId(1L)
                .quantity(100)
                .build();

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .items(List.of(itemRequestDto))
                .build();

        // Arrange Mocks
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Assert
        assertThrows(IllegalArgumentException.class,() -> orderService.createOrder(orderRequestDto));

        verify(orderRepository,never()).save(any());

    }
}
