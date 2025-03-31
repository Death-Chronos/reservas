package com.reservas.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.exceptions.DataReservaInvalidaException;
import com.reservas.exceptions.DataReservadaException;
import com.reservas.models.Quarto;
import com.reservas.models.Reserva;
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
        if (verificarDisponilibidade(reserva, quarto.getReservas())) {
            reserva.setQuarto(quarto);
            return reservaRepo.save(reserva);
        }else{
            throw new DataReservadaException("A data requerida já está reservada.");
        }
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

    private boolean verificarIntervaloDatas(Reserva reserva){
        return reserva.getInicio().isBefore(reserva.getFim());
    }

}