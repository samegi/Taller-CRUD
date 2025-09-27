package com.example.demo.service;

import com.example.demo.model.Creature;
import com.example.demo.repository.CreatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;

    @Autowired
    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    // Crear una nueva criatura
    public Creature createCreature(Creature creature) {
        return creatureRepository.save(creature);
    }

    // Listar todas las criaturas
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    // Buscar una criatura por ID
    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Creature not found"));
    }

    // Actualizar una criatura
    public Creature updateCreature(Long id, Creature updatedCreature) {
        Creature creature = getCreatureById(id);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        return creatureRepository.save(creature);
    }

    // Eliminar una criatura
    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        }else{
		throw new IllegalStateException("Cannot delete a creature in critical health");
}    
}
}
