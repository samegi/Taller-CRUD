package com.example.demo.repository;

import com.example.demo.model.Creature; //Importa la entidad Creature
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository para operaciones CRUD
import org.springframework.stereotype.Repository; // Marca esta interfaz como un repositorio

@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {

}
