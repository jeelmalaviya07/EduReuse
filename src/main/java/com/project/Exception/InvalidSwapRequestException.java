package com.project.Exception;

public class InvalidSwapRequestException extends RuntimeException {
    public InvalidSwapRequestException(String message) {
        super(message);
    }
}
