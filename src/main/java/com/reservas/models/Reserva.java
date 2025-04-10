package com.reservas.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservas.models.enums.StatusReserva;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private StatusReserva status;
    @NotNull(message = "Data de início não pode ser nula.")
    private LocalDate inicio;
    @NotNull(message = "Data de fim não pode ser nula.")
    private LocalDate fim;
    
    @ManyToOne
    @JoinColumn(name = "quarto_id")
    @JsonIgnore
    private Quarto quarto;

    public Reserva(@NotNull(message = "Data de início não pode ser nula.") LocalDate inicio,
            @NotNull(message = "Data de fim não pode ser nula.") LocalDate fim, Quarto quarto) {
        this.status = StatusReserva.ATIVA;
        this.inicio = inicio;
        this.fim = fim;
        this.quarto = quarto;
    }

    

}
