package com.dev.week7.exceptions;

public class PaymentMethodNotFoundException extends RuntimeException{

    public PaymentMethodNotFoundException(){
        super("We can't find the PaymentMethod with the given id");
    }

    public PaymentMethodNotFoundException(String message){
        super(message);
    }

}
