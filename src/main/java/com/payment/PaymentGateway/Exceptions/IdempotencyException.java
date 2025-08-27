package com.payment.PaymentGateway.Exceptions;

public class IdempotencyException extends RuntimeException{
    public IdempotencyException(String msg){
        super(msg);
    }
}
