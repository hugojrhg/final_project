package com.dev.week7.model.interfaces;

import com.dev.week7.exceptions.InsufficientFundsException;

public interface IPaymentMethod {
    boolean reserveFunds(Double price) throws InsufficientFundsException;

    boolean payDebt();
}
