package com.dev.week7.model.order;

import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.model.product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutDTO {

    @NotNull(message = "A customer is needed to create a checkout")
    private Customer customer;
    @NotNull(message = "A product is needed to create a checkout")
    private Product product;
    @NotNull(message = "A quantity to the product is needed to create a checkout")
    private Integer quantity;
    private PaymentMethod paymentMethod;
    private Integer zipCode;

}
