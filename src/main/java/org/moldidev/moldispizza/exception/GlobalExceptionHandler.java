package org.moldidev.moldispizza.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgumentException(InvalidArgumentException exception, WebRequest request) {
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

    @ExceptionHandler({BadCredentialsException.class, SignatureException.class, ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleSecurityUnauthorizedExceptions(Exception exception, WebRequest request) {
        ErrorResponse message = new ErrorResponse();
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if (exception instanceof BadCredentialsException) {
            message.setErrorMessage("Invalid credentials");
        }

        else if (exception instanceof SignatureException) {
            message.setErrorMessage("JWT signature validation failed");
        }

        else if (exception instanceof ExpiredJwtException) {
            message.setErrorMessage("JWT expired");
        }

        message.setCreatedAt(new Date());
        message.setErrorCode(status.value());
        message.setErrorDescription(request.getDescription(false));

        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleSecurityForbiddenExceptions(WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                "You are not authorized to access this resource",
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
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
