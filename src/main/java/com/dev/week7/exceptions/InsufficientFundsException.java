package com.dev.week7.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(){
        super("You don't have the sufficient funds");
    }

    public InsufficientFundsException(String message){
        super(message);
    }

}
