package com.reservas.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Quarto {
    
    @Id
    @GeneratedValue
    private Long id;
    private Integer numero;
    private Set<Reserva> reservas = new HashSet<Reserva>();
}
