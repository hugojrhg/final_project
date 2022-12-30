package com.dev.week7.model.payment;

import lombok.Data;

@Data
public class CreditCard extends PaymentMethod {
    private Integer number;

    public CreditCard(Double funds, Integer number) {
        super(funds, "CreditCard");
        this.number = number;
    }

}
