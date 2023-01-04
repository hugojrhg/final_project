package com.dev.week7.controller;

import com.dev.week7.exceptions.CheckoutNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.CheckoutDTO;
import com.dev.week7.model.order.CheckoutProduct;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.model.product.Product;
import com.dev.week7.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing Checkouts Endpoints")
class CheckoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CheckoutService checkoutService;

    @MockBean
    ModelMapper mapper;

    Checkout emptyCheckout;
    Checkout checkout;
    CheckoutDTO checkoutDTO;

    static final String JSON_CONTENT = "{'customer':{'id':1,'email':hugo@email,'firstName':hugo,'lastName':emanuel,'zipCodes':[123],'paymentMethods':[{'name':null,'funds':null,'reservedFunds':0.0}]},'paymentMethod':{'name':null,'funds':null,'reservedFunds':0.0},'zipCode':123}";

    //static final String INPUT_CONTENT = "{\"customer\":{\"id\":1,\"email\":\"hugo@email\",\"firstName\":\"hugo\",\"lastName\":\"emanuel\",\"zipCodes\":[123],\"paymentMethods\":[{\"name\":null,\"funds\":null,\"reservedFunds\":0.0}]},\"product\":{\"id\":1,\"name\":\"Milk\",\"price\":10.0,\"quantity\":5},\"quantity\":5,\"paymentMethod\":{\"name\":null,\"funds\":null,\"reservedFunds\":0.0},\"zipCode\":123}";
    static final String INPUT_CONTENT = "{\"customer\":{\"id\":1,\"email\":\"hugo@email\",\"firstName\":\"hugo\",\"lastName\":\"emanuel\",\"zipCodes\":[123],\"paymentMethods\":[{\"name\":null,\"funds\":null,\"reservedFunds\":0.0}]},\"paymentMethod\":{\"name\":null,\"funds\":null,\"reservedFunds\":0.0},\"zipCode\":123}";

    @BeforeEach
    void setUp() {
        startCheckout();
    }

    @Nested
    @DisplayName("Testing GET endpoints")
    class GetTest {

        @Test
        @WithMockUser
        void givenAnValidAccessToken_WhenGetAllCheckouts_ReturnAllCheckoutsAnd_200_OK() throws Exception {
            when(checkoutService.getAllCheckouts()).thenReturn(List.of(checkout));

            List<Checkout> listResult = checkoutService.getAllCheckouts();

            mockMvc.perform(get("/checkouts"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[" + JSON_CONTENT + "]"));

            assertThat(listResult.size(), is(1));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetAllCheckouts_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/checkouts"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnValidAccessTokenAndAnExistentId_WhenGetCheckoutById_ReturnAnCheckoutAnd_200_OK() throws Exception {
            when(checkoutService.getCheckoutById(anyLong())).thenReturn(checkout);

            Checkout Checkout = checkoutService.getCheckoutById(1L);

            mockMvc.perform(get("/checkouts/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(JSON_CONTENT));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetCheckoutById_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/checkouts/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonexistentId_WhenGetCheckoutById_ReturnAn_404_NotFound() throws Exception {
            when(checkoutService.getCheckoutById(anyLong())).thenThrow(CheckoutNotFoundException.class);

            mockMvc.perform(get("/Checkouts/1"))
                    .andExpect(status().isNotFound());
        }


        @Test
        void getCheckoutById() {
        }

    }

    @Nested
    @DisplayName("Testing POST endpoints")
    class PostTest {

        @Test
        @WithMockUser
        void givenAnCorrectCheckout_WhenCreateCheckout_ReturnTheCreatedCheckoutAndAn_201_CREATED() throws Exception {
            mockMvc.perform(post("/checkouts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"customer\":{\"id\":1},\"product\":{\"id\":1},\"quantity\":5}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{'customer':{'id':1},'product':{'id':1},'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenCreateCheckout_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(post("/checkouts"))
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
        void givenAnExistentId_WhenDeleteCheckout_ReturnAn_204_NO_CONTENT() throws Exception {
            when(checkoutService.getCheckoutById(anyLong())).thenReturn(checkout);

            mockMvc.perform(delete("/checkouts/1")).andExpect(status().isNoContent());
        }

        @Test
        void givenAnIncorrectAccessToken_WhenDeleteCheckout_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(delete("/checkouts/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonExistentId_WhenDeleteCheckout_ReturnAn_404_NOT_FOUND() throws Exception {
            doThrow(CheckoutNotFoundException.class).when(checkoutService).deleteCheckout(anyLong());

            mockMvc.perform(delete("/checkouts/1"))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing PUT endpoints")
    class PutTest {

        @Test
        @WithMockUser
        void givenAnCorrectCheckoutAndAnExistentId_WhenUpdateCheckout_ReturnTheUpdatedCheckoutAndAn_200_OK() throws Exception {
            when(checkoutService.updateCheckout(any(), anyLong())).thenReturn(checkout);

            mockMvc.perform(put("/checkouts/1").contentType(MediaType.APPLICATION_JSON).content("{\"customer\":{\"id\":1},\"product\":{\"id\":1},\"quantity\":5}").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'customer':{'id':1},'product':{'id':1},'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdateCheckout_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(put("/checkouts/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectCheckout_WhenUpdateCheckout_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(put("/checkouts/1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void givenAnUnExistentId_WhenUpdateCheckout_ReturnAn_404_NOT_FOUND() throws Exception {
            when(checkoutService.updateCheckout(any(), anyLong()))
                    .thenThrow(CheckoutNotFoundException.class);

            mockMvc.perform(put("/checkouts/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"customer\":{\"id\":1},\"product\":{\"id\":1},\"quantity\":5}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing PATCH endpoints")
    class PatchTest {

        @Test
        @WithMockUser
        void givenAnCorrectField_WhenUpdateCheckoutFields_ReturnAn_200_OK() throws Exception {
            when(checkoutService.updateCheckoutFields(any(), anyLong())).thenReturn(checkout);

            mockMvc.perform(patch("/checkouts/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"quantity\":5}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'customer':null,'product':null,'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdateCustomerFields_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(patch("/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"firstName\":\"hugo2\"}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnInvalidBody_WhenUpdateCustomerFields_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(patch("/customers/1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void addProductToCheckout() throws Exception {

        }

        @Test
        void removeProductFromCheckout() {
        }

        @Test
        void updateProductQuantityInCheckout() {
        }

    }


    void startCheckout() {
        PaymentMethod paymentMethod = new PaymentMethod();
        Customer customer = new Customer(1L, "hugo@email", "hugo", "emanuel", List.of(123), List.of(paymentMethod));
        Product product = new Product("Milk", 10.0, 5);
        CheckoutProduct checkoutProduct = new CheckoutProduct();
        checkoutProduct.setProduct(product);
        checkoutProduct.setQuantity(5);

        emptyCheckout = new Checkout();
        checkout = new Checkout(1L, customer, List.of(checkoutProduct), paymentMethod, 123);
    }
}