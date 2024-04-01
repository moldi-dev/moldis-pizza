package org.moldidev.moldispizza.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
