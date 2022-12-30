package com.dev.week7.model.interfaces;

public interface IPaymentMethod {
    boolean reserveFunds(Double price) throws Exception;

    boolean payDebt();
}
