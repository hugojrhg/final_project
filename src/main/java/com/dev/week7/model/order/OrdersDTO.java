package com.dev.week7.model.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersDTO {

    @NotNull
    private Checkout checkout;

}
