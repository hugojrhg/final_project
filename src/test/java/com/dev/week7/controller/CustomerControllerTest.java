package com.dev.week7.controller;

import com.dev.week7.exceptions.CustomerNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
@DisplayName("Testing Customers Endpoints")
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    Customer customer;

    @BeforeEach
    void setUp() {
        startCustomers();
    }

    @Nested
    @DisplayName("Testing GET endpoints")
    class GetTest {

        @Test
        @WithMockUser
        void givenAnValidAccessToken_WhenGetAllCustomers_ReturnAllCustomersAnd_200_OK() throws Exception {
            when(customerService.getAllCustomers()).thenReturn(List.of(customer));

            List<Customer> listResult = customerService.getAllCustomers();

            mockMvc.perform(get("/customers"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{'firstName':'hugo','lastName':'emanuel','zipCodes':[123],'paymentMethods':[{'name':null,'funds':null,'reservedFunds':0.0}]}]"));

            assertThat(listResult.size(), is(1));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetAllCustomers_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/customers"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnValidAccessTokenAndAnExistentId_WhenGetCustomerById_ReturnAnCustomerAnd_200_OK() throws Exception {
            when(customerService.getCustomerById(Mockito.anyLong())).thenReturn(customer);

            Customer customer = customerService.getCustomerById(1L);

            mockMvc.perform(get("/customers/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'firstName':'hugo','lastName':'emanuel','zipCodes':[123],'paymentMethods':[{'name':null,'funds':null,'reservedFunds':0.0}]}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetCustomerById_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/customers/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonexistentId_WhenGetCustomerById_ReturnAn_404_NotFound() throws Exception {
            when(customerService.getCustomerById(anyLong())).thenThrow(CustomerNotFoundException.class);

            mockMvc.perform(get("/customers/1"))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing POST endpoints")
    class PostTest {

        @Test
        @WithMockUser
        void givenAnCorrectCustomer_WhenCreateCustomer_ReturnTheCreatedCustomerAndAn_201_CREATED() throws Exception {
            mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content("{\"firstName\":\"hugo\",\"lastName\":\"emanuel\",\"zipCodes\":[123],\"paymentMethods\":[{\"name\":null,\"funds\":null,\"reservedFunds\":0.0}]}").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{'firstName':'hugo','lastName':'emanuel','zipCodes':[123],'paymentMethods':[{'name':null,'funds':null,'reservedFunds':0.0}]}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenCreateCustomer_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(post("/customers"))
                    .andExpect(status()
                            .isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectCustomer_WhenCreateCustomer_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(post("/customers")
                            .content(""))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("Testing DELETE endpoints")
    class DeleteTest {

        @Test
        @WithMockUser
        void givenAnExistentId_WhenDeleteCustomer_ReturnAn_204_NO_CONTENT() throws Exception {
            when(customerService.getCustomerById(anyLong())).thenReturn(customer);

            mockMvc.perform(delete("/customers/1")).andExpect(status().isNoContent());
        }

        @Test
        void givenAnIncorrectAccessToken_WhenDeleteCustomer_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(delete("/customers/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonExistentId_WhenDeleteCustomer_ReturnAn_404_NOT_FOUND() throws Exception {
            doThrow(CustomerNotFoundException.class).when(customerService).deleteCustomer(anyLong());

            mockMvc.perform(delete("/customers/1"))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("Testing PUT endpoints")
    class PutTest {

        @Test
        @WithMockUser
        void givenAnCorrectCustomerAndAnExistentId_WhenUpdateCustomer_ReturnTheUpdatedCustomerAndAn_200_OK() throws Exception {
            when(customerService.updateCustomer(any(), anyLong())).thenReturn(customer);

            mockMvc.perform(put("/customers/1").contentType(MediaType.APPLICATION_JSON).content("{\"firstName\":\"hugo\",\"lastName\":\"emanuel\",\"zipCodes\":[123],\"paymentMethods\":[{\"name\":null,\"funds\":null,\"reservedFunds\":0.0}]}").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andExpect(content().json("{'firstName':'hugo','lastName':'emanuel','zipCodes':[123],'paymentMethods':[{'name':null,'funds':null,'reservedFunds':0.0}]}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdateCustomer_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(put("/customers/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectCustomer_WhenUpdateCustomer_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(put("/customers/1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void givenAnUnExistentId_WhenUpdateCustomer_ReturnAn_404_NOT_FOUND() throws Exception {
            when(customerService.updateCustomer(any(), anyLong()))
                    .thenThrow(CustomerNotFoundException.class);

            mockMvc.perform(put("/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"firstName\":\"hugo\",\"lastName\":\"emanuel\",\"zipCodes\":[123],\"paymentMethods\":[{\"name\":null,\"funds\":null,\"reservedFunds\":0.0}]}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing PATCH endpoints")
    class PatchTest {

        @Test
        @WithMockUser
        void givenAnCorrectField_WhenUpdateCustomerFields_ReturnAn_200_OK() throws Exception {
            when(customerService.updateCustomerFields(any(), anyLong())).thenReturn(customer);

            mockMvc.perform(patch("/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"firstName\":\"hugo2\"}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'firstName':'hugo2','lastName':null,'zipCodes':null}"));
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

    }


    void startCustomers() {
        PaymentMethod paymentMethod = new PaymentMethod();
        customer = new Customer(1L, "hugo@email", "hugo", "emanuel", List.of(123), List.of(paymentMethod));
    }

}