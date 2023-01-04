package com.dev.week7.controller;

import com.dev.week7.exceptions.ProductNotFoundException;
import com.dev.week7.exceptions.ProductNotFoundException;
import com.dev.week7.exceptions.ProductNotFoundException;
import com.dev.week7.model.product.Product;
import com.dev.week7.service.ProductService;
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
@DisplayName("Testing Products Endpoints")
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    Product product;

    @BeforeEach
    void setUp() {
        startProducts();
    }

    @Nested
    @DisplayName("Testing GET endpoints")
    class GetTest {

        @Test
        @WithMockUser
        void givenAnValidAccessToken_WhenGetAllProducts_ReturnAllProductsAnd_200_OK() throws Exception {
            when(productService.getAllProducts()).thenReturn(List.of(product));

            List<Product> listResult = productService.getAllProducts();

            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{'name':'Milk','price':10.0,'quantity':5}]"));

            assertThat(listResult.size(), is(1));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetAllProducts_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/products"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnValidAccessTokenAndAnExistentId_WhenGetProductById_ReturnAnProductAnd_200_OK() throws Exception {
            when(productService.getProductById(Mockito.anyLong())).thenReturn(product);

            Product product = productService.getProductById(1L);

            mockMvc.perform(get("/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'name':'Milk','price':10.0,'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenGetProductById_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(get("/products/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonexistentId_WhenGetProductById_ReturnAn_404_NotFound() throws Exception {
            when(productService.getProductById(anyLong())).thenThrow(ProductNotFoundException.class);

            mockMvc.perform(get("/products/1"))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing POST endpoints")
    class PostTest {

        @Test
        @WithMockUser
        void givenAnCorrectProduct_WhenCreateProduct_ReturnTheCreatedProductAndAn_201_CREATED() throws Exception {
            mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Milk\",\"price\":10.0,\"quantity\":5}").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{'name':'Milk','price':10.0,'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenCreateProduct_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(post("/products"))
                    .andExpect(status()
                            .isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectProduct_WhenCreateProduct_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(post("/products")
                            .content(""))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("Testing DELETE endpoints")
    class DeleteTest {

        @Test
        @WithMockUser
        void givenAnExistentId_WhenDeleteProduct_ReturnAn_204_NO_CONTENT() throws Exception {
            when(productService.getProductById(anyLong())).thenReturn(product);

            mockMvc.perform(delete("/products/1")).andExpect(status().isNoContent());
        }

        @Test
        void givenAnIncorrectAccessToken_WhenDeleteProduct_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(delete("/products/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnNonExistentId_WhenDeleteProduct_ReturnAn_404_NOT_FOUND() throws Exception {
            doThrow(ProductNotFoundException.class).when(productService).deleteProduct(anyLong());

            mockMvc.perform(delete("/products/1"))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("Testing PUT endpoints")
    class PutTest {

        @Test
        @WithMockUser
        void givenAnCorrectProductAndAnExistentId_WhenUpdateProduct_ReturnTheUpdatedProductAndAn_200_OK() throws Exception {
            when(productService.updateProduct(anyLong(), any())).thenReturn(product);

            mockMvc.perform(put("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Milk\",\"price\":10.0,\"quantity\":5}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andExpect(content().json("{'name':'Milk','price':10.0,'quantity':5}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdateProduct_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(put("/products/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnIncorrectProduct_WhenUpdateProduct_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(put("/products/1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void givenAnUnExistentId_WhenUpdateProduct_ReturnAn_404_NOT_FOUND() throws Exception {
            when(productService.updateProduct(anyLong(), any()))
                    .thenThrow(ProductNotFoundException.class);

            mockMvc.perform(put("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Milk\",\"price\":10.0,\"quantity\":5}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Testing PATCH endpoints")
    class PatchTest {

        @Test
        @WithMockUser
        void givenAnCorrectField_WhenUpdateProductFields_ReturnAn_200_OK() throws Exception {
            when(productService.updateProductFields(any(), anyLong())).thenReturn(product);

            mockMvc.perform(patch("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Egg\"}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{'name':'Egg','price':null,'quantity':null}"));
        }

        @Test
        void givenAnInvalidAccessToken_WhenUpdateProductFields_ReturnAn_401_UNAUTHORIZED() throws Exception {
            mockMvc.perform(patch("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Egg\"}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void givenAnInvalidBody_WhenUpdateProductFields_ReturnAn_400_BAD_REQUEST() throws Exception {
            mockMvc.perform(patch("/products/1"))
                    .andExpect(status().isBadRequest());
        }

    }

    void startProducts() {
        product = new Product("Milk", 10.0, 5);
    }
}