package com.example.ewallet.notification.exception;

import lombok.Getter;
@Getter
public class TransactionException extends RuntimeException {

    private String type;
    private String message;

    public TransactionException(String type, String message) {
        super(message);
        this.type = type;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
