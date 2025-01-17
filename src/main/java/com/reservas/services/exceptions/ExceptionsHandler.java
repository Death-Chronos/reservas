package com.reservas.services.exceptions;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.reservas.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionsHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionMessage> ResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String erro = "Recurso não encontrado";
        ArrayList<String> detalhes = new ArrayList<String>();
        detalhes.add(e.getMessage());
        ExceptionMessage mensagem = new ExceptionMessage(Instant.now(), status.value(), erro, request.getRequestURI(), detalhes);
        return ResponseEntity.status(status).body(mensagem);
    }
}
