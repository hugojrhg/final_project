package com.dev.week7.model.payment;

import com.dev.week7.model.customer.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentMethodDTO {

    @NotNull
    private String name;
    @NotNull
    private Double funds;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Customer customer;

}
