package com.reservas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservas.models.Quarto;

public interface QuartoRepository  extends JpaRepository<Quarto, Long>{
    
}
