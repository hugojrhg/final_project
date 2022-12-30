package com.dev.week7.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException{

    public CustomerAlreadyExistsException(){
        super("Already exists a customer whit the given email");
    }

    public CustomerAlreadyExistsException(String message){
        super(message);
    }

}
