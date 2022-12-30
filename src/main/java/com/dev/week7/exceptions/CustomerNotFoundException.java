package com.dev.week7.exceptions;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException(){
        super("We can't find a customer whit the given ID");
    }

    public CustomerNotFoundException(String message){
        super(message);
    }

}
