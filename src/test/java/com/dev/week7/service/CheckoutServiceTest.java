package com.dev.week7.service;

import com.dev.week7.exceptions.CheckoutNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.CheckoutProduct;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.model.product.Product;
import com.dev.week7.repository.CheckoutRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Testing Checkout Methods")
class CheckoutServiceTest {

    @InjectMocks
    CheckoutService checkoutService;

    @Mock
    CheckoutRepository checkoutRepository;

    Checkout checkout;
    CheckoutProduct checkoutProduct;
    Customer customer;

    @BeforeEach
    void setUp() {
        startCheckouts();
    }

    @Nested
    @DisplayName("Testing GET Methods")
    class GetMethods {

        @Test
        void whenGetAllCheckouts_ReturnAllCheckouts() {
            when(checkoutRepository.findAll()).thenReturn(List.of(checkout));

            List<Checkout> resultList = checkoutService.getAllCheckouts();

            assertAll(
                    () -> assertThat(resultList.size(), is(1)),
                    () -> assertThat(resultList.get(0), isA(Checkout.class)),
                    () -> assertThat(resultList.get(0), is(checkout))
            );
        }

        @Test
        void givenAnExistentId_WhenGetCheckoutById_ReturnTheCheckout() {
            when(checkoutRepository.findById(anyLong())).thenReturn(Optional.ofNullable(checkout));

            Checkout result = checkoutService.getCheckoutById(1L);

            assertAll(
                    () -> assertThat(result, is(checkout)),
                    () -> assertThat(result, isA(Checkout.class))
            );
        }

        @Test
        void givenAnUnExistentId_WhenGetCheckoutById_ReturnAnCheckoutNotFoundException() {
            Exception exception = assertThrows(CheckoutNotFoundException.class, () -> {
                checkoutService.getCheckoutById(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a checkout whit the given ID"));
        }

    }

    @Nested
    @DisplayName("Testing Create Methods")
    class CreateTest {

        @Test
        void givenAnCorrectCheckout_WhenCreateCheckout_ReturnTheCreatedCheckout() {
            when(checkoutRepository.save(any())).thenReturn(checkout);

            Checkout result = checkoutService.createCheckout(checkout);

            assertAll(
                    () -> assertThat(result, isA(Checkout.class)),
                    () -> assertThat(result, is(checkout))
            );

        }

    }

    @Nested
    @DisplayName("Testing Delete Methods")
    class DeleteTest {

        @Test
        void givenAnExistentId_WhenDeleteCheckout_ShouldDeleteWithSuccess() {
            when(checkoutRepository.findById(anyLong())).thenReturn(Optional.of(checkout));

            doNothing().when(checkoutRepository).deleteById(1L);

            checkoutService.deleteCheckout(1L);

            verify(checkoutRepository, times(1)).delete(checkout);
        }

        @Test
        void givenAnUnExistentId_WhenDeleteCheckout_ReturnAnCheckoutNotFoundException() {
            Exception exception = assertThrows(CheckoutNotFoundException.class, () -> {
                checkoutService.deleteCheckout(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a checkout whit the given ID"));
        }

    }

    @Nested
    @DisplayName("Testing Update Methods")
    class UpdateTest {

        @Test
        void givenAnExistentId_WhenUpdateCheckout_ReturnTheUpdatedCheckout() {
            when(checkoutRepository.findById(anyLong())).thenReturn(Optional.of(checkout));
            when(checkoutRepository.save(any())).thenReturn(checkout);

            checkout.setZipCode(345);
            Checkout result = checkoutService.updateCheckout(checkout, 1L);

            assertAll(
                    () -> assertThat(result, is(checkout)),
                    () -> assertThat(result.getZipCode(), is(345))
            );

        }

        @Test
        void givenAnUnExistentId_WhenUpdateCheckout_ReturnAnCheckoutNotFoundException() {
            Exception exception = assertThrows(CheckoutNotFoundException.class, () -> {
                checkoutService.updateCheckout(checkout, 1L);
            });
        }

    }

    void startCheckouts() {
        PaymentMethod paymentMethod = new PaymentMethod();
        customer = new Customer(1L, "hugo@email", "hugo", "emanuel", List.of(123), List.of(paymentMethod));
        Product product = new Product("Milk", 10.0, 5);
        CheckoutProduct checkoutProduct = new CheckoutProduct();
        checkoutProduct.setProduct(product);
        checkoutProduct.setQuantity(5);

        checkout = new Checkout(1L, customer, List.of(checkoutProduct), paymentMethod, 123);
    }
}