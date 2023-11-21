package com.projeto_pi.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerController {

    private final Map<String, String> RESPONSE = new LinkedHashMap<>();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof NotFoundException || e instanceof NoSuchElementException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (e instanceof Unauthorized) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof Forbidden) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else if (e instanceof BadRequest || e instanceof IllegalArgumentException || e instanceof InternalAuthenticationServiceException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e instanceof MethodNotAllowedException || e instanceof HttpRequestMethodNotSupportedException) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (e instanceof DataAccessException || e instanceof RuntimeException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        RESPONSE.put("error", e.getMessage().contains("No value present") ? "Usuário não encontrado" : e.getMessage());

        return new ResponseEntity<>(RESPONSE, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {

        FieldError firstFieldError = e.getBindingResult().getFieldError();

        if (firstFieldError != null) {
            RESPONSE.put("error", firstFieldError.getDefaultMessage());
            return new ResponseEntity<>(RESPONSE, HttpStatus.BAD_REQUEST);
        }

        RESPONSE.put("error", e.getMessage());

        return new ResponseEntity<>(RESPONSE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        RESPONSE.put("error", "Acesso negado: %s".formatted(e.getMessage().contains("Acesso negado") ? "Não autorizado" : e.getMessage()));
        return new ResponseEntity<>(RESPONSE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        RESPONSE.put("error", "Falha na autenticação: %s".formatted(e.getMessage().contains("No value present") ? "Usuário não encontrado" : e.getMessage()));
        return new ResponseEntity<>(RESPONSE, HttpStatus.UNAUTHORIZED);
    }
}
