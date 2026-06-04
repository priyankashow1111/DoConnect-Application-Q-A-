package com.doconnect.notification_service.exception;

public class EmailSendException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public EmailSendException(String message) {
        super(message);
    }
}