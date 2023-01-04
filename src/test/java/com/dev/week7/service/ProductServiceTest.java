package com.dev.week7.service;

import com.dev.week7.exceptions.ProductNotFoundException;
import com.dev.week7.model.product.Product;
import com.dev.week7.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Testing Product Service")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startProducts();
    }

    @Nested
    @DisplayName("Testing GET Methods")
    class GetMethods {

        @Test
        void whenGetAllProducts_ReturnAllProducts() {
            when(productRepository.findAll()).thenReturn(List.of(product));

            List<Product> resultList = productService.getAllProducts();

            assertAll(
                    () -> assertThat(resultList.size(), is(1)),
                    () -> assertThat(resultList.get(0), isA(Product.class)),
                    () -> assertThat(resultList.get(0), is(product))
            );
        }

        @Test
        void givenAnExistentId_WhenGetProductById_ReturnTheProduct() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

            Product result = productService.getProductById(1L);

            assertAll(
                    () -> assertThat(result, is(product)),
                    () -> assertThat(result, isA(Product.class))
            );
        }

        @Test
        void givenAnUnExistentId_WhenGetProductById_ReturnAnProductNotFoundException() {
            Exception exception = assertThrows(ProductNotFoundException.class, () -> {
                productService.getProductById(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a product with the given id"));
        }

    }

    @Nested
    @DisplayName("Testing Create Methods")
    class CreateTest {

        @Test
        void givenAnCorrectProduct_WhenCreateProduct_ReturnTheCreatedProduct() {
            when(productRepository.save(any())).thenReturn(product);

            Product result = productService.createProduct(product);

            assertAll(
                    () -> assertThat(result, isA(Product.class)),
                    () -> assertThat(result, is(product))
            );

        }

    }

    @Nested
    @DisplayName("Testing Delete Methods")
    class DeleteTest {

        @Test
        void givenAnExistentId_WhenDeleteProduct_ShouldDeleteWithSuccess() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

            doNothing().when(productRepository).deleteById(1L);

            productService.deleteProduct(1L);

            verify(productRepository, times(1)).deleteById(1L);
        }

        @Test
        void givenAnUnExistentId_WhenDeleteProduct_ReturnAnProductNotFoundException() {
            Exception exception = assertThrows(ProductNotFoundException.class, () -> {
                productService.deleteProduct(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a product with the given id"));
        }

    }

    @Nested
    @DisplayName("Testing Update Methods")
    class UpdateTest {

        @Test
        void givenAnExistentId_WhenUpdateProduct_ReturnTheUpdatedProduct() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            when(productRepository.save(any())).thenReturn(product);

            product.setName("Egg");
            Product result = productService.updateProduct(1L, product);

            assertAll(
                    () -> assertThat(result, is(product)),
                    () -> assertThat(result.getName(), is("Egg"))
            );

        }

        @Test
        void givenAnUnExistentId_WhenUpdateProduct_ReturnAnProductNotFoundException() {
            Exception exception = assertThrows(ProductNotFoundException.class, () -> {
                productService.updateProduct(1L, product);
            });
        }

        @Test
        void updateProductFields() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(any())).thenReturn(product);

            Product newProduct = new Product("Egg", null, null);
            Product result = productService.updateProductFields(newProduct, 1L);

            assertAll(
                    () -> assertThat(result.getName(), is("Egg")),
                    () -> assertThat(result.getPrice(), is(10.0)),
                    () -> assertThat(result, is(product))
            );
        }

        @Test
        void updateQuantity() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(any())).thenReturn(product);

            productService.updateQuantity(1L, 50);

            assertThat(product.getQuantity(), is(50));
        }

    }

    void startProducts() {
        product = new Product("Milk", 10.0, 5);
    }
}