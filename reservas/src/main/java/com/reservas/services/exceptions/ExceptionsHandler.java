package com.reservas.services.exceptions;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.reservas.exceptions.DataReservaInvalidaException;
import com.reservas.exceptions.DataReservadaException;
import com.reservas.exceptions.RecursoNaoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionsHandler {
    
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ExceptionMessage> RecursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String erro = "Recurso não encontrado";
        ArrayList<String> detalhes = new ArrayList<String>();
        detalhes.add(e.getMessage());
        ExceptionMessage mensagem = new ExceptionMessage(Instant.now(), status.value(), erro, request.getRequestURI(), detalhes);
        return ResponseEntity.status(status).body(mensagem);
    }

    @ExceptionHandler(DataReservadaException.class)
    public ResponseEntity<ExceptionMessage> DataReservada(DataReservadaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.OK;
        String erro = "Já existe uma reserva nessa data";
        ArrayList<String> detalhes = new ArrayList<String>();
        detalhes.add(e.getMessage());
        ExceptionMessage mensagem = new ExceptionMessage(Instant.now(), status.value(), erro, request.getRequestURI(), detalhes);
        return ResponseEntity.status(status).body(mensagem);
    }
    @ExceptionHandler(DataReservaInvalidaException.class)
    public ResponseEntity<ExceptionMessage> DataReservada(DataReservaInvalidaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.OK;
        String erro = "A data inicial deve ser anterior à data final";
        ArrayList<String> detalhes = new ArrayList<String>();
        detalhes.add(e.getMessage());
        ExceptionMessage mensagem = new ExceptionMessage(Instant.now(), status.value(), erro, request.getRequestURI(), detalhes);
        return ResponseEntity.status(status).body(mensagem);
    }
}
