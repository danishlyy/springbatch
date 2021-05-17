package com.example.springbatch.exception;



public class RetryException extends RuntimeException {

    private String code;

    private String msg;

    public RetryException(){
        super();
    }

    public RetryException(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
