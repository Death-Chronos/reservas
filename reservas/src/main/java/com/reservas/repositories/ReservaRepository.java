package com.reservas.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reservas.models.Reserva;
import com.reservas.models.enums.StatusReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFimBefore(LocalDate data);
    
    @Query("SELECT r FROM Reserva r WHERE r.quarto.id = :quartoId AND r.status = :status")
    Set<Reserva> buscarAtivasPorQuarto(@Param("quartoId") Long quartoId, @Param("status") StatusReserva status);

}
