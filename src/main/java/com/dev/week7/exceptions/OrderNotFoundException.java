package com.dev.week7.exceptions;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(){
        super("We can't find an Order whit the given ID");
    }

    public OrderNotFoundException(String message){
        super(message);
    }

}
