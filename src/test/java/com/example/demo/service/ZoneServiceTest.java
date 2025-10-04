package com.example.demo.service;

import com.example.demo.model.Zone;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.repository.CreatureRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private CreatureRepository creatureRepository;

    @InjectMocks
    private ZoneService zoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* CREATE */
    @Test
    void create_ShouldSave_WhenValid() {
        Zone z = new Zone();
        z.setName("Bosque");
        z.setDescription("Zona de criaturas mágicas");
        z.setCapacity(10);

        when(zoneRepository.existsByNameIgnoreCase("Bosque")).thenReturn(false);
        when(zoneRepository.save(any(Zone.class))).thenAnswer(inv -> inv.getArgument(0));

        Zone result = zoneService.create(z);

        assertNotNull(result);
        assertEquals("Bosque", result.getName());
        assertEquals(10, result.getCapacity());
        verify(zoneRepository).save(any(Zone.class));
    }

    @Test
    void create_ShouldThrow_WhenNameBlank() {
        Zone z = new Zone();
        z.setName("");
        z.setCapacity(5);

        assertThrows(IllegalArgumentException.class, () -> zoneService.create(z));
        verify(zoneRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrow_WhenCapacityNegative() {
        Zone z = new Zone();
        z.setName("Mar");
        z.setCapacity(-3);

        assertThrows(IllegalArgumentException.class, () -> zoneService.create(z));
        verify(zoneRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrow_WhenDuplicateName() {
        Zone z = new Zone();
        z.setName("Bosque");

        when(zoneRepository.existsByNameIgnoreCase("Bosque")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> zoneService.create(z));
        verify(zoneRepository, never()).save(any());
    }

    /* READ */
    @Test
    void getAll_ShouldReturnList() {
        when(zoneRepository.findAll()).thenReturn(List.of(new Zone(), new Zone()));
        List<Zone> zones = zoneService.getAll();
        assertEquals(2, zones.size());
    }

    @Test
    void getById_ShouldReturn_WhenExists() {
        Zone expected = new Zone();
        expected.setId(1L);
        expected.setName("Montaña");

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(expected));

        Zone result = zoneService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Montaña", result.getName());
    }

    @Test
    void getById_ShouldThrow_WhenNotExists() {
        when(zoneRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> zoneService.getById(999L));
    }

    /* UPDATE */
    @Test
    void update_ShouldModifyFields_WhenValid() {
        Zone existing = new Zone();
        existing.setId(1L);
        existing.setName("Viejo");
        existing.setCapacity(10);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(creatureRepository.countByZoneId(1L)).thenReturn(5L);
        when(zoneRepository.save(any(Zone.class))).thenAnswer(inv -> inv.getArgument(0));

        Zone update = new Zone();
        update.setName("Nuevo");
        update.setDescription("Actualizada");
        update.setCapacity(8);

        Zone result = zoneService.update(1L, update);

        assertEquals("Nuevo", result.getName());
        assertEquals("Actualizada", result.getDescription());
        assertEquals(8, result.getCapacity());
    }

    @Test
    void update_ShouldThrow_WhenZoneNotFound() {
        when(zoneRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> zoneService.update(100L, new Zone()));
    }

    @Test
    void update_ShouldThrow_WhenCapacityNegative() {
        Zone existing = new Zone();
        existing.setId(1L);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existing));

        Zone bad = new Zone();
        bad.setCapacity(-2);

        assertThrows(IllegalArgumentException.class, () -> zoneService.update(1L, bad));
    }

    @Test
    void update_ShouldThrow_WhenCapacityLessThanAssigned() {
        Zone existing = new Zone();
        existing.setId(1L);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(creatureRepository.countByZoneId(1L)).thenReturn(5L);

        Zone bad = new Zone();
        bad.setCapacity(2);

        assertThrows(IllegalStateException.class, () -> zoneService.update(1L, bad));
    }

    @Test
    void update_ShouldThrow_WhenDuplicateName() {
        Zone existing = new Zone();
        existing.setId(1L);
        existing.setName("Original");

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(zoneRepository.existsByNameIgnoreCase("Duplicado")).thenReturn(true);

        Zone bad = new Zone();
        bad.setName("Duplicado");

        assertThrows(IllegalStateException.class, () -> zoneService.update(1L, bad));
    }

    /* DELETE */
    @Test
    void delete_ShouldRemove_WhenNoCreaturesAssigned() {
        Zone z = new Zone();
        z.setId(1L);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(z));
        when(creatureRepository.countByZoneId(1L)).thenReturn(0L);

        zoneService.delete(1L);

        verify(zoneRepository).delete(z);
    }

    @Test
    void delete_ShouldThrow_WhenCreaturesAssigned() {
        Zone z = new Zone();
        z.setId(1L);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(z));
        when(creatureRepository.countByZoneId(1L)).thenReturn(3L);

        assertThrows(IllegalStateException.class, () -> zoneService.delete(1L));
        verify(zoneRepository, never()).delete(any());
    }

    /* COUNT UTILITY */
    @Test
    void countCreaturesInZone_ShouldReturnRepositoryValue() {
        when(creatureRepository.countByZoneId(1L)).thenReturn(7L);

        long result = zoneService.countCreaturesInZone(1L);

        assertEquals(7L, result);
        verify(creatureRepository).countByZoneId(1L);
    }
}
