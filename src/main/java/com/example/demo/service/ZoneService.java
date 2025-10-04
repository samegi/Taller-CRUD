package com.example.demo.service;

import com.example.demo.model.Zone;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.repository.CreatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final CreatureRepository creatureRepository;

    public ZoneService(ZoneRepository zoneRepository, CreatureRepository creatureRepository) {
        this.zoneRepository = zoneRepository;
        this.creatureRepository = creatureRepository;
    }

    /* CREATE */
    public Zone create(Zone z) {
        if (z.getName() == null || z.getName().isBlank())
            throw new IllegalArgumentException("El nombre de la zona es obligatorio");

        if (z.getCapacity() != null && z.getCapacity() < 0)
            throw new IllegalArgumentException("La capacidad no puede ser negativa");

        if (zoneRepository.existsByNameIgnoreCase(z.getName()))
            throw new IllegalStateException("Ya existe una zona con ese nombre");

        return zoneRepository.save(z);
    }

    /* READ (todas) */
    public List<Zone> getAll() {
        return zoneRepository.findAll();
    }

    /* READ (una) */
    public Zone getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Zona no encontrada"));
    }

    /* UPDATE */
    public Zone update(Long id, Zone data) {
        Zone z = getById(id); // valida existencia

        if (data.getName() != null && !data.getName().isBlank()) {
            if (!data.getName().equalsIgnoreCase(z.getName())
                    && zoneRepository.existsByNameIgnoreCase(data.getName())) {
                throw new IllegalStateException("Ya existe una zona con ese nombre");
            }
            z.setName(data.getName());
        }

        if (data.getDescription() != null) {
            z.setDescription(data.getDescription());
        }

        if (data.getCapacity() != null) {
            if (data.getCapacity() < 0) {
                throw new IllegalArgumentException("La capacidad no puede ser negativa");
            }
            long asignadas = creatureRepository.countByZoneId(id);
            if (data.getCapacity() < asignadas) {
                throw new IllegalStateException(
                        "La capacidad no puede ser menor que las criaturas asignadas (" + asignadas + ")"
                );
            }
            z.setCapacity(data.getCapacity());
        }

        return zoneRepository.save(z);
    }

    /* DELETE */
    public void delete(Long id) {
        Zone z = getById(id); // valida existencia antes de borrar
        long asignadas = creatureRepository.countByZoneId(id);
        if (asignadas > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar: la zona tiene criaturas asignadas (" + asignadas + ")"
            );
        }
        zoneRepository.delete(z);
    }

    /* Utilidad: cantidad de criaturas en una zona */
    public long countCreaturesInZone(Long zoneId) {
        return creatureRepository.countByZoneId(zoneId);
    }
}
