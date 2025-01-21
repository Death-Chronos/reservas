package com.reservas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.exceptions.ResourceNotFoundException;
import com.reservas.models.Quarto;
import com.reservas.repositories.QuartoRepository;

@Service
public class QuartoService {
    
    @Autowired
    private QuartoRepository quartoRepo;

    public Quarto criarQuarto(Quarto quarto){
        return quartoRepo.save(quarto);
    }
    
    public Quarto getQuartoPorId(Long id){
        return quartoRepo.findById(id).orElseThrow(
            ()-> new ResourceNotFoundException("Quarto não encontrado com o id: " + id));
    }

}
