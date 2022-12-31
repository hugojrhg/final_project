package com.dev.week7.exceptions;

public class IncompleteCheckoutException extends RuntimeException{

    public IncompleteCheckoutException(){
        super("You need a Payment Method and a ZipCode in the Checkout to create a new Order");
    }

    public IncompleteCheckoutException(String message){
        super(message);
    }

}
