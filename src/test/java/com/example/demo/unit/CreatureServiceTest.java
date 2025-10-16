package com.example.demo.unit;

import com.example.demo.model.Creature;
import com.example.demo.repository.CreatureRepository;
import com.example.demo.service.CreatureService;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCreatureById_ShouldReturnCreature_WhenCreatureExists() {
        Long creatureId = 1L;
        Creature expectedCreature = new Creature();
        expectedCreature.setId(creatureId);

        when(creatureRepository.findById(creatureId)).thenReturn(Optional.of(expectedCreature));

        Creature actualCreature = creatureService.getCreatureById(creatureId);

        assertNotNull(actualCreature);
        assertEquals(expectedCreature, actualCreature);
        verify(creatureRepository, times(1)).findById(creatureId);
    }

    @Test
    void testGetCreatureById_ShouldThrowException_WhenCreatureDoesNotExist() {
        Long creatureId = 2L;
        when(creatureRepository.findById(creatureId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> creatureService.getCreatureById(creatureId));

        verify(creatureRepository, times(1)).findById(creatureId);
    }

}