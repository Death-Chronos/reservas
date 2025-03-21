package com.reservas.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
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

    public List<Quarto> getAllQuartos(){
        return quartoRepo.findAll();
    }

    public Quarto atualizarQuarto(Long id, Quarto updatedQuarto) {
        if (quartoExisteById(id)) {
            Quarto quarto = getQuartoPorId(id);
            BeanUtils.copyProperties(updatedQuarto, quarto);
            return quartoRepo.save(quarto);
        }
        throw new ResourceNotFoundException("Quarto não encontrado com o id: " + id);
    }

    public void deletarQuarto(Long id){
        if(quartoExisteById(id)){
            quartoRepo.deleteById(id);
        }
            throw new ResourceNotFoundException("Quarto não encontrado com o id: " + id);        
    }
    private Boolean quartoExisteById(Long id){
        return quartoRepo.existsById(id);
    }
}
