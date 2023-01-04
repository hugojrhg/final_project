package com.dev.week7.controller;

import com.dev.week7.exceptions.CheckoutNotFoundException;
import com.dev.week7.exceptions.OrderNotFoundException;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.Orders;
import com.dev.week7.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing Orders Endpoints")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    Orders order;

    @BeforeEach
    void setUp() {
        startOrders();
    }

    @Nested
    @DisplayName("Testing GET endpoints")
    class GetTest{

        @Test
        @WithMockUser
        void givenAnValidAccessToken_WhenGetAllOrders_ReturnAllOrdersAndAn_200_OK() throws Exception {
            when(orderService.getAllOrders()).thenReturn(List.of(order));

            List<Orders> ordersList = orderService.getAllOrders();

            mockMvc.perform(get("/orders"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{}]"));

            assertThat(ordersList.size(), is(1));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetAllOrders_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/orders"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnValidAccessTokenAndAnExistentId_WhenGetOrdersById_ReturnAn_200_OK() throws Exception {
            when(orderService.getOrdersById(anyLong())).thenReturn(order);

            mockMvc.perform(get("/orders/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetCheckoutById_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/checkouts/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonexistentId_WhenGetCheckoutById_ReturnAn_404_NotFound() throws Exception {
            when(orderService.getOrdersById(anyLong())).thenThrow(OrderNotFoundException.class);

            mockMvc.perform(get("/Checkouts/1"))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing POST endpoints")
    class PostTest {

        @Test
        @WithMockUser
        void givenAnCorrectOrder_WhenCreateOrder_ReturnTheCreatedOrderAndAn_201_CREATED() throws Exception {
            mockMvc.perform(post("/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"checkout\":{\"id\":1}}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{'checkout':{'customer':null,'paymentMethod':null,'zipCode':null}}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenCreateCheckout_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(post("/orders"))
                    .andExpect(status()
                            .isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectCheckout_WhenCreateCheckout_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(post("/checkouts")
                            .content(""))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("Testing DELETE endpoints")
    class DeleteTest {

        @Test
        @WithMockUser
        void givenAnExistentId_WhenDeleteOrder_ReturnAn_204_NO_CONTENT() throws Exception {
            when(orderService.getOrdersById(anyLong())).thenReturn(order);

            mockMvc.perform(delete("/orders/1")).andExpect(status().isNoContent());
        }

        @Test
        void givenAnIncorrectAccessToken_WhenDeleteCheckout_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(delete("/checkouts/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonExistentId_WhenDeleteCheckout_ReturnAn_404_NOT_FOUND() throws Exception {
            doThrow(CheckoutNotFoundException.class).when(orderService).deleteOrders(anyLong());

            mockMvc.perform(delete("/orders/1"))
                    .andExpect(status().isNotFound());
        }

    }

    private void startOrders() {
        Checkout checkout = new Checkout();
        order = new Orders(checkout);
    }
}