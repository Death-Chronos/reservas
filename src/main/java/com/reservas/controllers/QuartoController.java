package com.reservas.controllers;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<String> deletarQuarto(@PathVariable Long id){
        quartoService.deletarQuarto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Quarto deletado com sucesso.");
    }

    @PostMapping("/{quartoId}/reservar")
    public ResponseEntity<Reserva> reservarQuarto(@PathVariable Long quartoId, @RequestBody @Valid Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.saveReserva(quartoId, reserva));
    }

    @GetMapping("/{quartoId}/reservas")
    public ResponseEntity<Set<Reserva>> getReservasPorQuartoId(@PathVariable Long quartoId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.getReservasPorQuartoId(quartoId));
    }

    @DeleteMapping("/reservas/deletar/{id}")
    public ResponseEntity<String> deletarReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reserva deletada com sucesso.");
    }

    @DeleteMapping("/reservas/cancelar/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reserva cancelada com sucesso.");
    }


}
