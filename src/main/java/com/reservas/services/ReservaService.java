package com.reservas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.exceptions.DataReservaInvalidaException;
import com.reservas.exceptions.DataReservadaException;
import com.reservas.exceptions.RecursoNaoEncontradoException;
import com.reservas.models.Quarto;
import com.reservas.models.Reserva;
import com.reservas.models.enums.StatusReserva;
import com.reservas.repositories.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepo;

    @Autowired
    private QuartoService quartoService;

    public Reserva saveReserva(Long quartoId, Reserva reserva) {
        if (!verificarIntervaloDatas(reserva)) {
            throw new DataReservaInvalidaException("A data de início deve ser anterior à data de fim.");
        }

        Quarto quarto = quartoService.getQuartoPorId(quartoId);

        Set<Reserva> reservasAtivas = quarto.getReservas().stream()
                .filter(r -> reserva.getStatus() == StatusReserva.ATIVA).collect(Collectors.toSet());
        if (verificarDisponilibidade(reserva, reservasAtivas)) {
            reserva.setQuarto(quarto);
            return reservaRepo.save(reserva);
        } else {
            throw new DataReservadaException("A data requerida já está reservada.");
        }
    }

    public Reserva getReservaPorId(Long id) {
        return reservaRepo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada."));
    }

    public Set<Reserva> getReservasPorQuartoId(Long quartoId) {
        Quarto quarto = quartoService.getQuartoPorId(quartoId);
        return quarto.getReservas();
    }

    public void deleteReserva(Long id) {
        if (!existebyId(id))
            throw new RecursoNaoEncontradoException("Reserva não encontrada.");

        Reserva reserva = getReservaPorId(id);
        reservaRepo.delete(reserva);
    }

    public void cancelarReserva(Long id) {
        if (!existebyId(id))
            throw new RecursoNaoEncontradoException("Reserva não encontrada.");
        Reserva reserva = getReservaPorId(id);
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepo.save(reserva);
    }

    public void finalizarReservas(){
        List<Reserva> reservas = reservaRepo.findByFimAfter(LocalDate.now());
        reservas.forEach(reserva -> reserva.setStatus(StatusReserva.FINALIZADA));
        reservaRepo.saveAll(reservas);
    }

    private boolean verificarDisponilibidade(Reserva novaReserva, Set<Reserva> reservasExistentes) {
        for (Reserva reservaExistente : reservasExistentes) {
            if (!(novaReserva.getFim().isBefore(reservaExistente.getInicio()) ||
                    novaReserva.getInicio().isAfter(reservaExistente.getFim()))) {
                return false;
            }
        }
        return true;
    }

    private boolean verificarIntervaloDatas(Reserva reserva) {
        return reserva.getInicio().isBefore(reserva.getFim());
    }

    private boolean existebyId(Long id) {
        return reservaRepo.existsById(id);
    }
}