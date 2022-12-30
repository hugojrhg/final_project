package com.dev.week7.exceptions;

public class SoldOutProductException extends RuntimeException{

    public SoldOutProductException(){
        super("The product is sold out or don't have the quantity that you need");
    }

    public SoldOutProductException(String message){
        super(message);
    }

}
