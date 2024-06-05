package org.moldidev.moldispizza.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgumentException(InvalidInputException exception, WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadCredentialsException.class, SignatureException.class, ExpiredJwtException.class, InsufficientAuthenticationException.class, AccessDeniedException.class, AccountStatusException.class})
    public ResponseEntity<ErrorResponse> handleSecurityExceptions(Exception ex, WebRequest request) {
        ErrorResponse message = new ErrorResponse();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof BadCredentialsException) {
            message.setErrorMessage("Invalid credentials");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (ex instanceof SignatureException) {
            message.setErrorMessage("The JWT signature is invalid");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (ex instanceof ExpiredJwtException) {
            message.setErrorMessage("The JWT has expired");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (ex instanceof InsufficientAuthenticationException || ex instanceof AccessDeniedException) {
            message.setErrorMessage("You are not authorized to access this resource");
            status = HttpStatus.FORBIDDEN;
        }

        else if (ex instanceof AccountStatusException) {
            message.setErrorMessage("This account is locked");
            status = HttpStatus.FORBIDDEN;
        }

        message.setCreatedAt(new Date());
        message.setErrorCode(status.value());
        message.setErrorDescription(request.getDescription(false));

        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
