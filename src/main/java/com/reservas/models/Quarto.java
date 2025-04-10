package com.reservas.models;

import java.util.HashSet;
import java.util.Set;

import com.reservas.models.enums.ClasssificacaoQuarto;
import com.reservas.models.enums.TipoQuarto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Número do quarto não pode ser nulo.")
    private Integer numero;
    @NotNull(message = "Tipo do quarto não pode ser nulo.")
    private TipoQuarto tipo;
    @NotNull(message = "Classificação do quarto não pode ser nulo.")
    private ClasssificacaoQuarto classificacao;
    @NotNull(message = "Preço do quarto não pode ser nulo.")
    private Double preco;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reserva> reservas = new HashSet<Reserva>();

    public Quarto(@NotNull(message = "Número do quarto não pode ser nulo.") Integer numero,
            @NotNull(message = "Tipo do quarto não pode ser nulo.") TipoQuarto tipo,
            @NotNull(message = "Classificação do quarto não pode ser nulo.") ClasssificacaoQuarto classificacao,
            @NotNull(message = "Preço do quarto não pode ser nulo.") Double preco) {
        this.numero = numero;
        this.tipo = tipo;
        this.classificacao = classificacao;
        this.preco = preco;
        this.reservas = new HashSet<Reserva>();
    }

    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
        reserva.setQuarto(this);
    }
}
