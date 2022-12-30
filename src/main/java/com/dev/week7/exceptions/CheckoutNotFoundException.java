package com.dev.week7.exceptions;

public class CheckoutNotFoundException extends RuntimeException {

    public CheckoutNotFoundException(){
        super("We can't find a checkout whit the given ID");
    }

    public CheckoutNotFoundException(String message){
        super(message);
    }

}
