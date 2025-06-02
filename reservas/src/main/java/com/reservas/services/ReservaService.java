package com.reservas.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.reservas.config.RabbitMQConfig;
import com.reservas.exceptions.DataReservaInvalidaException;
import com.reservas.exceptions.DataReservadaException;
import com.reservas.exceptions.RecursoNaoEncontradoException;
import com.reservas.models.Quarto;
import com.reservas.models.Reserva;
import com.reservas.models.enums.StatusReserva;
import com.reservas.repositories.ReservaRepository;

@Service
@EnableScheduling
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepo;

    @Autowired
    private QuartoService quartoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Reserva saveReserva(Long quartoId, Reserva reserva) {
        if (verificarIntervaloDatas(reserva)) {
            throw new DataReservaInvalidaException("A data de início deve ser anterior ou igual à data de fim.");
        }

        Quarto quarto = quartoService.getQuartoPorId(quartoId);

        Set<Reserva> reservasAtivas = reservaRepo.buscarAtivasPorQuarto(quartoId, StatusReserva.ATIVA);
        if (verificarDisponilibidade(reserva, reservasAtivas)) {
            reserva.setQuarto(quarto);
            reserva.setStatus(StatusReserva.ATIVA);

            Reserva novaReserva = reservaRepo.save(reserva);
            agendarFinalizacaoReserva(novaReserva.getId().toString() ,
                    ChronoUnit.MILLIS.between(LocalDate.now().atStartOfDay(),
                            novaReserva.getFim().atStartOfDay()));

            return novaReserva;
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

    // @Scheduled(cron = "0 0 00 * * *")
    // public void finalizarReservasExpiradas() {
    // List<Reserva> reservas = reservaRepo.findByFimBefore(LocalDate.now());
    // reservas.forEach(reserva -> reserva.setStatus(StatusReserva.FINALIZADA));
    // reservaRepo.saveAll(reservas);
    // }

    public void agendarFinalizacaoReserva(String reservaId, long millisDelay) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELAY_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                reservaId,
                message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(millisDelay));
                    return message;
                });
    }

    @RabbitListener(queues = RabbitMQConfig.FINAL_QUEUE)
    public void finalizarReserva(String reservaId) {
        Reserva reserva = reservaRepo.findById(Long.valueOf(reservaId)).orElse(null);
        if (reserva != null && reserva.getFim().isBefore(LocalDate.now())) {
            reserva.setStatus(StatusReserva.FINALIZADA);
            reservaRepo.save(reserva);
            System.out.println("Reserva finalizada automaticamente: " + reservaId);
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

    private boolean verificarIntervaloDatas(Reserva reserva) {
        return reserva.getInicio().isAfter(reserva.getFim()) ||
                reserva.getInicio().isEqual(reserva.getFim());
    }

    private boolean existebyId(Long id) {
        return reservaRepo.existsById(id);
    }
}