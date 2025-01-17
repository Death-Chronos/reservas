package com.reservas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservas.models.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
}
