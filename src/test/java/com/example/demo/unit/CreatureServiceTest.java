package com.example.demo.unit;

import com.example.demo.model.Creature;
import com.example.demo.service.CreatureService;
import com.example.demo.repository.CreatureRepository;
import com.example.demo.exception.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;  // JUnit 5 assertions
import static org.mockito.Mockito.*;              // Mockito

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreatureServiceTest {

    @Mock
    private CreatureRepository creatureRepository;

    @InjectMocks
    private CreatureService creatureService;

    private Creature creature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        creature = new Creature();
        creature.setId(1L);
        creature.setName("Dragon");
        creature.setSpecies("Reptile");
        creature.setSize(10);
        creature.setDangerLevel(7);
        creature.setHealthStatus("stable");
    }

    @Test
    void testCreateCreature_Success() {
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature saved = creatureService.createCreature(creature);

        assertNotNull(saved);
        assertEquals("Dragon", saved.getName());
        verify(creatureRepository, times(1)).save(creature);
    }

    @Test
    void testCreateCreature_InvalidDangerLevel_ThrowsException() {
        creature.setDangerLevel(15);
        assertThrows(IllegalArgumentException.class, () -> creatureService.createCreature(creature));
        verify(creatureRepository, never()).save(any());
    }

    @Test
    void testGetCreatureById_Found() {
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        Creature found = creatureService.getCreatureById(1L);

        assertEquals("Dragon", found.getName());
        verify(creatureRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCreatureById_NotFound() {
        when(creatureRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> creatureService.getCreatureById(99L));
    }

    @Test
    void testUpdateCreature_Success() {
        Creature updated = new Creature();
        updated.setName("Phoenix");
        updated.setSpecies("Bird");
        updated.setSize(8);
        updated.setDangerLevel(5);
        updated.setHealthStatus("healthy");

        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));
        when(creatureRepository.save(any(Creature.class))).thenReturn(updated);

        Creature result = creatureService.updateCreature(1L, updated);

        assertEquals("Phoenix", result.getName());
        assertEquals(5, result.getDangerLevel());
        verify(creatureRepository, times(1)).save(any(Creature.class));
    }

    @Test
    void testUpdateCreature_InvalidSize_ThrowsException() {
        Creature updated = new Creature();
        updated.setSize(-3);

        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        assertThrows(IllegalArgumentException.class, () -> creatureService.updateCreature(1L, updated));
        verify(creatureRepository, never()).save(any());
    }

    @Test
    void testDeleteCreature_Success() {
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        creatureService.deleteCreature(1L);

        verify(creatureRepository, times(1)).delete(creature);
    }

    @Test
    void testDeleteCreature_CriticalHealth_ThrowsException() {
        creature.setHealthStatus("critical");
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        assertThrows(IllegalArgumentException.class, () -> creatureService.deleteCreature(1L));
        verify(creatureRepository, never()).delete(any());
    }
}