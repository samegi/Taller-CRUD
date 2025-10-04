package com.example.demo.service;

import com.example.demo.model.Creature;
import com.example.demo.repository.CreatureRepository;
import com.example.demo.exception.ResourceNotFoundException;

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

    // Validaciones de negocio
    private void validateCreature(Creature creature) {
        if (creature.getSize() < 0) {
            throw new IllegalArgumentException("El tamaño no puede ser menor que 0");
        }
        if (creature.getDangerLevel() < 1 || creature.getDangerLevel() > 10) {
            throw new IllegalArgumentException("El nivel de peligro debe estar entre 1 y 10");
        }
    }

    // Crear una nueva criatura
    public Creature createCreature(Creature creature) {
        validateCreature(creature);
        return creatureRepository.save(creature);
    }

    // Listar todas las criaturas
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    // Buscar una criatura por ID
    public Creature getCreatureById(Long id) {
    return creatureRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Criatura no encontrada con id: " + id));
}

    // Actualizar una criatura
    public Creature updateCreature(Long id, Creature updatedCreature) {
        // 🔹 Primero verificar si existe
        Creature creature = getCreatureById(id);
        // 🔹 Luego validar datos
        validateCreature(updatedCreature);

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
        if (!"critical".equalsIgnoreCase(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalArgumentException("No se puede eliminar una criatura con estado crítico");
        }
    }
}
