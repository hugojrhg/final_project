package com.dev.week7.service;

import com.dev.week7.exceptions.PaymentMethodNotFoundException;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.repository.PaymentMethodRepository;
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
@DisplayName("Testing Payments Service")
class PaymentMethodServiceTest {

    @InjectMocks
    PaymentMethodService paymentMethodService;

    @Mock
    PaymentMethodRepository paymentMethodRepository;

    PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        startPayments();
    }

    @Nested
    @DisplayName("Testing GET Methods")
    class GetMethods {

        @Test
        void whenGetAllPaymentMethods_ReturnAllPaymentMethods() {
            when(paymentMethodRepository.findAll()).thenReturn(List.of(paymentMethod));

            List<PaymentMethod> resultList = paymentMethodService.getAllPaymentMethods();

            assertAll(
                    () -> assertThat(resultList.size(), is(1)),
                    () -> assertThat(resultList.get(0), isA(PaymentMethod.class)),
                    () -> assertThat(resultList.get(0), is(paymentMethod))
            );
        }

        @Test
        void givenAnExistentId_WhenGetPaymentMethodById_ReturnThePaymentMethod() {
            when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.ofNullable(paymentMethod));

            PaymentMethod result = paymentMethodService.getPaymentMethodById(1L);

            assertAll(
                    () -> assertThat(result, is(paymentMethod)),
                    () -> assertThat(result, isA(PaymentMethod.class))
            );
        }

        @Test
        void givenAnUnExistentId_WhenGetPaymentMethodById_ReturnAnPaymentMethodNotFoundException() {
            Exception exception = assertThrows(PaymentMethodNotFoundException.class, () -> {
                paymentMethodService.getPaymentMethodById(1L);
            });

            assertThat(exception.getMessage(), is("We can't find the PaymentMethod with the given id"));
        }

    }

    @Nested
    @DisplayName("Testing Create Methods")
    class CreateTest {

        @Test
        void givenAnCorrectPaymentMethod_WhenCreatePaymentMethod_ReturnTheCreatedPaymentMethod() {
            when(paymentMethodRepository.save(any())).thenReturn(paymentMethod);

            PaymentMethod result = paymentMethodService.createPaymentMethod(paymentMethod);

            assertAll(
                    () -> assertThat(result, isA(PaymentMethod.class)),
                    () -> assertThat(result, is(paymentMethod))
            );

        }

    }

    @Nested
    @DisplayName("Testing Delete Methods")
    class DeleteTest {

        @Test
        void givenAnExistentId_WhenDeletePaymentMethod_ShouldDeleteWithSuccess() {
            when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));

            doNothing().when(paymentMethodRepository).deleteById(1L);

            paymentMethodService.deletePaymentMethod(1L);

            verify(paymentMethodRepository, times(1)).delete(paymentMethod);
        }

        @Test
        void givenAnUnExistentId_WhenDeletePaymentMethod_ReturnAnPaymentMethodNotFoundException() {
            Exception exception = assertThrows(PaymentMethodNotFoundException.class, () -> {
                paymentMethodService.deletePaymentMethod(1L);
            });

            assertThat(exception.getMessage(), is("We can't find the PaymentMethod with the given id"));
        }

    }

    @Nested
    @DisplayName("Testing Update Methods")
    class UpdateTest {

        @Test
        void givenAnExistentId_WhenUpdatePaymentMethod_ReturnTheUpdatedPaymentMethod() {
            when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
            when(paymentMethodRepository.save(any())).thenReturn(paymentMethod);

            paymentMethod.setName("card2");
            PaymentMethod result = paymentMethodService.updatePaymentMethod(paymentMethod, 1L);

            assertAll(
                    () -> assertThat(result, is(paymentMethod)),
                    () -> assertThat(result.getName(), is("card2"))
            );

        }

        @Test
        void givenAnUnExistentId_WhenUpdatePaymentMethod_ReturnAnPaymentMethodNotFoundException() {
            Exception exception = assertThrows(PaymentMethodNotFoundException.class, () -> {
                paymentMethodService.updatePaymentMethod(paymentMethod, 1L);
            });
        }

        @Test
        void updatePaymentMethodFields() {
            when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
            when(paymentMethodRepository.save(any())).thenReturn(paymentMethod);

            PaymentMethod newPaymentMethod = new PaymentMethod(null, "card2");
            PaymentMethod result = paymentMethodService.updatePaymentMethodFields(newPaymentMethod, 1L);

            assertAll(
                    () -> assertThat(result.getName(), is("card2")),
                    () -> assertThat(result.getFunds(), is(100.0)),
                    () -> assertThat(result, is(paymentMethod))
            );
        }

    }

    @Test
    void getAllPaymentMethods() {
    }

    @Test
    void getPaymentMethodById() {
    }

    @Test
    void createPaymentMethod() {
    }

    @Test
    void deletePaymentMethod() {
    }

    @Test
    void updatePaymentMethod() {
    }

    @Test
    void updatePaymentMethodFields() {
    }

    void startPayments() {
        paymentMethod = new PaymentMethod(100.0, "card");
    }
}