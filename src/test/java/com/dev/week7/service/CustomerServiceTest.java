package com.dev.week7.service;

import com.dev.week7.exceptions.CustomerNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.repository.CustomerRepository;
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
class CustomerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setUp() {
        startCustomers();
    }

    @Nested
    @DisplayName("Testing GET Methods")
    class GetMethods {

        @Test
        void whenGetAllCustomers_ReturnAllCustomers() {
            when(customerRepository.findAll()).thenReturn(List.of(customer));

            List<Customer> resultList = customerService.getAllCustomers();

            assertAll(
                    () -> assertThat(resultList.size(), is(1)),
                    () -> assertThat(resultList.get(0), isA(Customer.class)),
                    () -> assertThat(resultList.get(0), is(customer))
            );
        }

        @Test
        void givenAnExistentId_WhenGetCustomerById_ReturnTheCustomer() {
            when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));

            Customer result = customerService.getCustomerById(1L);

            assertAll(
                    () -> assertThat(result, is(customer)),
                    () -> assertThat(result, isA(Customer.class))
            );
        }

        @Test
        void givenAnUnExistentId_WhenGetCustomerById_ReturnAnCustomerNotFoundException() {
            Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
                customerService.getCustomerById(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a customer whit the given ID"));
        }

    }

    @Nested
    @DisplayName("Testing Create Methods")
    class CreateTest {

        @Test
        void givenAnCorrectCustomer_WhenCreateCustomer_ReturnTheCreatedCustomer() {
            when(customerRepository.save(any())).thenReturn(customer);

            Customer result = customerService.createCustomer(customer);

            assertAll(
                    () -> assertThat(result, isA(Customer.class)),
                    () -> assertThat(result, is(customer))
            );

        }

    }

    @Nested
    @DisplayName("Testing Update Methods")
    class UpdateTest {

        @Test
        void givenAnExistentId_WhenUpdateCustomer_ReturnTheUpdatedCustomer() {
            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            when(customerRepository.save(any())).thenReturn(customer);

            customer.setFirstName("hugo2");
            Customer result = customerService.updateCustomer(customer, 1L);

            assertAll(
                    () -> assertThat(result, is(customer)),
                    () -> assertThat(result.getFirstName(), is("hugo2"))
            );

        }

        @Test
        void givenAnUnExistentId_WhenUpdateCustomer_ReturnAnCustomerNotFoundException() {
            Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
                customerService.updateCustomer(customer, 1L);
            });
        }

        @Test
        void updateCustomerFields() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(customerRepository.save(any())).thenReturn(customer);

            Customer newCustomer = new Customer(null, null, "hugo2", null, null, null);
            Customer result = customerService.updateCustomerFields(newCustomer, 1L);

            assertAll(
                    () -> assertThat(result.getFirstName(), is("hugo2")),
                    () -> assertThat(result.getLastName(), is("Emanuel")),
                    () -> assertThat(result, is(customer))
            );
        }

    }

    @Nested
    @DisplayName("Testing Delete Methods")
    class DeleteTest {

        @Test
        void givenAnExistentId_WhenDeleteCustomer_ShouldDeleteWithSuccess() {
            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

            doNothing().when(customerRepository).deleteById(1L);

            customerService.deleteCustomer(1L);

            verify(customerRepository, times(1)).delete(customer);
        }

        @Test
        void givenAnUnExistentId_WhenDeleteCustomer_ReturnAnCustomerNotFoundException() {
            Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
                customerService.deleteCustomer(1L);
            });

            assertThat(exception.getMessage(), is("We can't find a customer whit the given ID"));
        }

    }

    void startCustomers() {
        customer = new Customer(1L, "hugo@email.com", "Hugo", "Emanuel", null, null);
    }
}