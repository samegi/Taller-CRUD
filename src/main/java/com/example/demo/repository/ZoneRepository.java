package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByNameIgnoreCase(String name);

}

