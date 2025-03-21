package com.reservas.models;

import java.util.HashSet;
import java.util.Set;

import com.reservas.models.enums.Tipo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer numero;
    private Tipo tipo;
    private Double preco;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reserva> reservas = new HashSet<Reserva>();

    public Quarto(Integer numero, Tipo tipo, Double preco, Set<Reserva> reservas) {
        this.numero = numero;
        this.tipo = tipo;
        this.preco = tipo.getPreco();
        this.reservas = reservas;
    }

    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
        reserva.setQuarto(this);
    }
}
