package com.lucas.accounts_service.exeptions;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
