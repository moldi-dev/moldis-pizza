package org.moldidev.moldispizza.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<HTTPResponse> handleObjectNotValidException(ObjectNotValidException exception, WebRequest request) {

        HTTPResponse response = HTTPResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false))
                .data(Map.of("validationErrors", exception.getViolations()))
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HTTPResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        HTTPResponse response = HTTPResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<HTTPResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request) {
        HTTPResponse response = HTTPResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .statusCode(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({MailAuthenticationException.class, BadCredentialsException.class, SignatureException.class, ExpiredJwtException.class, InsufficientAuthenticationException.class, AccessDeniedException.class, DisabledException.class, LockedException.class})
    public ResponseEntity<HTTPResponse> handleSecurityExceptions(Exception exception, WebRequest request) {
        HTTPResponse response = HTTPResponse.builder().build();
        HttpStatus status = null;

        if (exception instanceof MailAuthenticationException) {
            response.setMessage("Mail authentication failed");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        else if (exception instanceof BadCredentialsException) {
            response.setMessage("Invalid credentials");
            status = HttpStatus.NOT_FOUND;
        }

        else if (exception instanceof SignatureException) {
            response.setMessage("The JWT signature is invalid");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof ExpiredJwtException) {
            response.setMessage("The JWT has expired");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof InsufficientAuthenticationException || exception instanceof AccessDeniedException) {
            response.setMessage("You are not authorized to access this resource");
            status = HttpStatus.FORBIDDEN;
        }

        else if (exception instanceof DisabledException) {
            response.setMessage("This account is not yet verified. Follow the steps sent on the email to verify it");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof LockedException) {
            response.setMessage("This account is locked");
            status = HttpStatus.FORBIDDEN;
        }

        response.setTimestamp(LocalDateTime.now().toString());
        response.setStatusCode(status.value());
        response.setStatus(status);
        response.setPath(request.getDescription(false));

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HTTPResponse> handleGlobalException(Exception exception, WebRequest request) {
        HTTPResponse response = HTTPResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }
}
