package com.dev.week7.exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(){
        super("We can't find a product with the given id");
    }

    public ProductNotFoundException(String message){
        super(message);
    }

}
