package com.doconnect.user_service.exception;

public class QuestionClosedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public QuestionClosedException(String message) {
        super(message);
    }
}