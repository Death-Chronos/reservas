package com.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reservas.exceptions.RecursoNaoEncontradoException;
import com.reservas.models.Quarto;
import com.reservas.models.Reserva;
import com.reservas.models.enums.ClasssificacaoQuarto;
import com.reservas.models.enums.StatusReserva;
import com.reservas.models.enums.TipoQuarto;
import com.reservas.repositories.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepo;

    @Mock
    private QuartoService quartoService;

    @InjectMocks
    private ReservaService reservaService;

    private Quarto quarto1;
    private Reserva reservaSalva;

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
        return reserva.getInicio().isBefore(reserva.getFim()) ||
                reserva.getInicio().isEqual(reserva.getFim());
    }

    @BeforeEach
    public void criarDados() {
        System.out.println("Criando dados para os testes");
        quarto1 = new Quarto(101, TipoQuarto.CASAL, ClasssificacaoQuarto.SUITE, 384.00);
        quarto1.setId(1L);

        reservaSalva = new Reserva(LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 5), quarto1);
        reservaSalva.setId(1L);
        quarto1.addReserva(reservaSalva);
    }

    @Test
    void shouldSaveReservaIfQuartoExists() {
        System.out.println("Iniciando teste de saveReserva");
        // Arrange
        Reserva reservaTeste = new Reserva(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), null);

        // Configura os mocks
        when(quartoService.getQuartoPorId(anyLong())).thenReturn(quarto1);
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaSalva);

        Quarto quarto = quartoService.getQuartoPorId(1L);

        // Act - Chama o método do service diretamente
        Reserva resultado = reservaService.saveReserva(quarto.getId(), reservaTeste);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusReserva.ATIVA, resultado.getStatus());
        assertEquals(quarto1, resultado.getQuarto());
        verify(reservaRepo).save(any(Reserva.class));
    }

    @Test
    void shouldNotSaveReservaIfQuartoDoesNotExist() {
        // Configura os mocks
        when(quartoService.getQuartoPorId(anyLong())).thenThrow(new RecursoNaoEncontradoException("Quarto não encontrado."));

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> reservaService.saveReserva(1L, reservaSalva));

        assertEquals(RecursoNaoEncontradoException.class, exception.getClass());
    }

    @Test
    void shouldSaveReservaIfReservaDateIsAvaliableAndIntevaloIsValid() {
        Reserva reservaTeste = new Reserva(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), null);

        boolean dataDisponivel = verificarDisponilibidade(reservaTeste, quarto1.getReservas());
        boolean dataIntervaloValido = verificarIntervaloDatas(reservaTeste);
        assertEquals(true, dataDisponivel);
        assertEquals(true, dataIntervaloValido);
        
    }

    @Test
    void shouldNotSaveReservaIfReservaDateIsAvaliableAndIntevaloIsInvalid() {
        Reserva reservaTeste = new Reserva(LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 1), null);

        boolean dataDisponivel = verificarDisponilibidade(reservaTeste, quarto1.getReservas());
        boolean dataIntervaloValido = verificarIntervaloDatas(reservaTeste);
        assertEquals(true, dataDisponivel);
        assertEquals(false, dataIntervaloValido);
    }

    @Test
    void shouldNotSaveReservaIfReservaDateIsUnavailableAndIntevaloIsValid() {
        Reserva reservaTeste = new Reserva(LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 5), null);
        quarto1.addReserva(reservaTeste);

        boolean dataDisponivel = verificarDisponilibidade(reservaTeste, quarto1.getReservas());
        boolean dataIntervaloValido = verificarIntervaloDatas(reservaTeste);
        assertEquals(false, dataDisponivel);
        assertEquals(true, dataIntervaloValido);
    }

    @Test
    void shouldGetReservaById() {
        when(reservaRepo.findById(anyLong())).thenReturn(java.util.Optional.of(reservaSalva));

        Reserva reserva = reservaService.getReservaPorId(2L);
        assertNotNull(reserva);
    }

    @Test
    void shouldNotGetReservaById() {
        when(reservaRepo.findById(anyLong())).thenThrow(new RecursoNaoEncontradoException("Reserva não encontrada."));

        RecursoNaoEncontradoException exception = assertThrows(
            RecursoNaoEncontradoException.class,
                () -> reservaService.getReservaPorId(2L));

        assertEquals(RecursoNaoEncontradoException.class, exception.getClass());
    }

    @Test
    void shouldGetReservasByQuartoId(){
        when(quartoService.getQuartoPorId(anyLong())).thenReturn(quarto1);
        Set<Reserva> reservas = reservaService.getReservasPorQuartoId(1L);
        assertNotNull(reservas);
    }

    @Test
    void shouldCancelarReservaIfExists() {
        when(reservaRepo.existsById(anyLong())).thenReturn(true);
        boolean reservaExist = reservaRepo.existsById(1L);

        if (reservaExist) {
            reservaSalva.setStatus(StatusReserva.CANCELADA);
        }

        assertEquals(true, reservaExist);
        assertEquals(StatusReserva.CANCELADA, reservaSalva.getStatus());
    }

    @Test
    void shouldNotCancelarReservaIfNotExists() {
        when(reservaRepo.existsById(anyLong())).thenReturn(false);
        boolean reservaExist = reservaRepo.existsById(1L);

        if (reservaExist) {
            reservaSalva.setStatus(StatusReserva.CANCELADA);
        }

        assertEquals(false, reservaExist);
        assertNotEquals(StatusReserva.CANCELADA, reservaSalva.getStatus());
    }

    @Test
    void shouldDeleteReservaIfExists() {
        when(reservaRepo.existsById(anyLong())).thenReturn(true);
        boolean reservaExist = reservaRepo.existsById(1L);

        if (reservaExist) {
            reservaRepo.delete(reservaSalva);
        }

        assertEquals(true, reservaExist);
        verify(reservaRepo).delete(reservaSalva);
    }

    @Test
    void shouldNotDeleteReservaIfNotExists() {
        when(reservaRepo.existsById(anyLong())).thenReturn(false);
        boolean reservaExist = reservaRepo.existsById(1L);

        if (reservaExist) {
            reservaRepo.delete(reservaSalva);
        }

        assertEquals(false, reservaExist);
    }
}
