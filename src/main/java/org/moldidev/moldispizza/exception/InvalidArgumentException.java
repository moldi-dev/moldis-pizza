package org.moldidev.moldispizza.exception;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
