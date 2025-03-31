package com.reservas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reservas.models.Quarto;
import com.reservas.models.Reserva;
import com.reservas.services.QuartoService;
import com.reservas.services.ReservaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/quarto")
public class QuartoController {

    @Autowired
    QuartoService quartoService;

    @Autowired
    ReservaService reservaService;
    
    @PostMapping("/criar")
    public ResponseEntity<Quarto> criarQuarto(@RequestBody @Valid Quarto quarto){
        return ResponseEntity.status(HttpStatus.CREATED).body(quartoService.criarQuarto(quarto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quarto> getQuartoPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(quartoService.getQuartoPorId(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Quarto>> getAllQuartos(){
        return ResponseEntity.status(HttpStatus.OK).body(quartoService.getAllQuartos());
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Quarto> atualizarQuarto(@PathVariable Long id, @RequestBody @Valid Quarto quarto ){
        return ResponseEntity.status(HttpStatus.OK).body(quartoService.atualizarQuarto(id, quarto));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarQuarto(@PathVariable Long id){
        quartoService.deletarQuarto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{quartoId}/reservar")
    public ResponseEntity<Reserva> reservarQuarto(@PathVariable Long quartoId, @RequestBody @Valid Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.saveReserva(quartoId, reserva));
    }
}
