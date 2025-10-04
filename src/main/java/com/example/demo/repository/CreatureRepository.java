package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository; //Importa la entidad Creature
import org.springframework.stereotype.Repository; // JpaRepository para operaciones CRUD

import com.example.demo.model.Creature; // Marca esta interfaz como un repositorio

@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {
    long countByZoneId(Long zoneId);
}
