package com.example.demo.integration;

import com.example.demo.model.Creature;
import com.example.demo.service.CreatureService;
import com.example.demo.repository.CreatureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreatureTest {

    @Autowired
    private CreatureService creatureService;

    @Autowired
    private CreatureRepository creatureRepository;

    @Test
    void testCreateCreature_ShouldPersistInDatabase() {
        // Arrange
        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setSpecies("Mitológica");
        creature.setSize(2.5f);
        creature.setDangerLevel(5);
        creature.setHealthStatus("healthy");

        // Act
        creatureService.createCreature(creature);
        Optional<Creature> foundCreature = creatureRepository.findById(creature.getId());

        // Assert
        assertTrue(foundCreature.isPresent(), "La criatura debería haberse guardado en la base de datos");
        assertEquals("Unicornio", foundCreature.get().getName(), "El nombre no coincide");
    }

    @Test
    void testUpdateCreature_ShouldModifyValuesInDatabase() {
        // Arrange
        Creature creature = new Creature();
        creature.setName("Dragón");
        creature.setSpecies("Reptil");
        creature.setSize(10.0f);
        creature.setDangerLevel(8);
        creature.setHealthStatus("injured");
        creatureService.createCreature(creature);

        // Act
        Creature updated = new Creature();
        updated.setName("Dragón Rojo");
        updated.setSpecies("Reptil Volador");
        updated.setSize(12.0f);
        updated.setDangerLevel(9);
        updated.setHealthStatus("healthy");

        creatureService.updateCreature(creature.getId(), updated);

        // Assert
        Optional<Creature> foundCreature = creatureRepository.findById(creature.getId());
        assertTrue(foundCreature.isPresent(), "La criatura debería existir después de la actualización");
        assertEquals("Dragón Rojo", foundCreature.get().getName(), "El nombre no fue actualizado correctamente");
        assertEquals(12.0f, foundCreature.get().getSize(), "El tamaño no fue actualizado correctamente");
        assertEquals(9, foundCreature.get().getDangerLevel(), "El nivel de peligro no fue actualizado correctamente");
    }

    @Test
    void testDeleteCreature_ShouldRemoveFromDatabase() {
        // Arrange
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setSpecies("Ave Mítica");
        creature.setSize(3.0f);
        creature.setDangerLevel(6);
        creature.setHealthStatus("healthy");
        creatureService.createCreature(creature);

        Long id = creature.getId();

        // Act
        creatureService.deleteCreature(id);
        Optional<Creature> foundCreature = creatureRepository.findById(id);

        // Assert
        assertFalse(foundCreature.isPresent(), "La criatura debería haber sido eliminada de la base de datos");
    }
}
