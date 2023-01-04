package com.dev.week7.service;

import com.dev.week7.exceptions.OrderNotFoundException;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.Orders;
import com.dev.week7.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Testing Order Service")
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrdersRepository orderRepository;

    Orders order;

    @BeforeEach
    void setUp() {
        startOrders();
    }

    @Nested
    @DisplayName("Testing GET Methods")
    class GetMethods {

        @Test
        void whenGetAllOrders_ReturnAllOrders() {
            when(orderRepository.findAll()).thenReturn(List.of(order));

            List<Orders> resultList = orderService.getAllOrders();

            assertAll(
                    () -> assertThat(resultList.size(), is(1)),
                    () -> assertThat(resultList.get(0), isA(Orders.class)),
                    () -> assertThat(resultList.get(0), is(order))
            );
        }

        @Test
        void givenAnExistentId_WhenGetOrdersById_ReturnTheOrder() {
            when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

            Orders result = orderService.getOrdersById(1L);

            assertAll(
                    () -> assertThat(result, is(order)),
                    () -> assertThat(result, isA(Orders.class))
            );
        }

        @Test
        void givenAnUnExistentId_WhenGetOrdersById_ReturnAnOrderNotFoundException() {
            Exception exception = assertThrows(OrderNotFoundException.class, () -> {
                orderService.getOrdersById(1L);
            });

            assertThat(exception.getMessage(), is("We can't find an Order whit the given ID"));
        }

    }

    @Nested
    @DisplayName("Testing Delete Methods")
    class DeleteTest {

        @Test
        void givenAnExistentId_WhenDeleteOrders_ShouldDeleteWithSuccess() {
            when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

            doNothing().when(orderRepository).deleteById(1L);

            orderService.deleteOrders(1L);

            verify(orderRepository, times(1)).delete(order);
        }

        @Test
        void givenAnUnExistentId_WhenDeleteOrders_ReturnAnOrdersNotFoundException() {
            Exception exception = assertThrows(OrderNotFoundException.class, () -> {
                orderService.deleteOrders(1L);
            });

            assertThat(exception.getMessage(), is("We can't find an Order whit the given ID"));
        }

    }

    @Test
    void processOrderPayment() {
    }

    void startOrders() {
        Checkout checkout = new Checkout();
        order = new Orders(checkout);
    }
}