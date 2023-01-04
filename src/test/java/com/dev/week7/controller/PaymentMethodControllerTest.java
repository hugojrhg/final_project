package com.dev.week7.controller;

import com.dev.week7.exceptions.PaymentMethodNotFoundException;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.service.PaymentMethodService;
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
@DisplayName("Testing Payments Endpoints")
class PaymentMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentMethodService paymentMethodService;

    PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        startPaymentMethods();
    }


    @Nested
    @DisplayName("Testing GET endpoints")
    class GetTest {

        @Test
        @WithMockUser
        void givenAnValidAccessToken_WhenGetAllPaymentMethods_ReturnAllPaymentMethodsAnd_200_OK() throws Exception {
            when(paymentMethodService.getAllPaymentMethods()).thenReturn(List.of(paymentMethod));

            List<PaymentMethod> listResult = paymentMethodService.getAllPaymentMethods();

            mockMvc.perform(get("/payments"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{'funds':100.0,'name':'card'}]"));

            assertThat(listResult.size(), is(1));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetAllPaymentMethods_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/payments"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnValidAccessTokenAndAnExistentId_WhenGetPaymentMethodById_ReturnAnPaymentMethodAnd_200_OK() throws Exception {
            when(paymentMethodService.getPaymentMethodById(Mockito.anyLong())).thenReturn(paymentMethod);

            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(1L);

            mockMvc.perform(get("/payments/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'funds':100.0,'name':'card'}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetPaymentMethodById_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/payments/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonexistentId_WhenGetPaymentMethodById_ReturnAn_404_NotFound() throws Exception {
            when(paymentMethodService.getPaymentMethodById(anyLong())).thenThrow(PaymentMethodNotFoundException.class);

            mockMvc.perform(get("/payments/1"))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("Testing POST endpoints")
    class PostTest {

        @Test
        @WithMockUser
        void givenAnCorrectPaymentMethod_WhenCreatePaymentMethod_ReturnTheCreatedPaymentMethodAndAn_201_CREATED() throws Exception {
            mockMvc.perform(post("/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"funds\":100.0,\"name\":\"card\",\"customer\":{\"id\":1}}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{'funds':100.0,'name':'card'}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenCreatePaymentMethod_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(post("/payments"))
                    .andExpect(status()
                            .isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectPaymentMethod_WhenCreatePaymentMethod_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(post("/payments")
                            .content(""))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("Testing DELETE endpoints")
    class DeleteTest {

        @Test
        @WithMockUser
        void givenAnExistentId_WhenDeletePaymentMethod_ReturnAn_204_NO_CONTENT() throws Exception {
            when(paymentMethodService.getPaymentMethodById(anyLong())).thenReturn(paymentMethod);

            mockMvc.perform(delete("/payments/1")).andExpect(status().isNoContent());
        }

        @Test
        void givenAnIncorrectAccessToken_WhenDeletePaymentMethod_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(delete("/payments/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonExistentId_WhenDeletePaymentMethod_ReturnAn_404_NOT_FOUND() throws Exception {
            doThrow(PaymentMethodNotFoundException.class).when(paymentMethodService).deletePaymentMethod(anyLong());

            mockMvc.perform(delete("/payments/1"))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("Testing PUT endpoints")
    class PutTest {

        @Test
        @WithMockUser
        void givenAnCorrectPaymentMethodAndAnExistentId_WhenUpdatePaymentMethod_ReturnTheUpdatedPaymentMethodAndAn_200_OK() throws Exception {
            when(paymentMethodService.updatePaymentMethod(any(), anyLong())).thenReturn(paymentMethod);

            mockMvc.perform(put("/payments/1").contentType(MediaType.APPLICATION_JSON).content("{\"funds\":100.0,\"name\":\"card\",\"customer\":{\"id\":1}}").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andExpect(content().json("{'funds':100.0,'name':'card'}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdatePaymentMethod_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(put("/payments/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectPaymentMethod_WhenUpdatePaymentMethod_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(put("/payments/1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void givenAnUnExistentId_WhenUpdatePaymentMethod_ReturnAn_404_NOT_FOUND() throws Exception {
            when(paymentMethodService.updatePaymentMethod(any(), anyLong()))
                    .thenThrow(PaymentMethodNotFoundException.class);

            mockMvc.perform(put("/payments/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"funds\":100.0,\"name\":\"card\",\"customer\":{\"id\":1}}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing PATCH endpoints")
    class PatchTest {

        @Test
        @WithMockUser
        void givenAnCorrectField_WhenUpdatePaymentMethodFields_ReturnAn_200_OK() throws Exception {
            when(paymentMethodService.updatePaymentMethodFields(any(), anyLong())).thenReturn(paymentMethod);

            mockMvc.perform(patch("/payments/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"funds\":100.0,\"name\":\"card2\",\"customer\":{\"id\":1}}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'funds':100.0,'name':'card2'}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdatePaymentMethodFields_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(patch("/payments/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"firstName\":\"hugo2\"}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnInvalidBody_WhenUpdatePaymentMethodFields_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(patch("/payments/1"))
                    .andExpect(status().isBadRequest());
        }

    }

    void startPaymentMethods() {
        paymentMethod = new PaymentMethod(100.0, "card");
    }
}