package com.example.demo.model; // #Paquete del proyecto

import jakarta.persistence.Entity;     // JPA (para mapear a la base de datos)
import jakarta.persistence.GeneratedValue;             // Lombok (genera getters/setters/toString)
import jakarta.persistence.GenerationType; // Lombok (constructor vacío)
import jakarta.persistence.Id; // Importa la entidad Zone
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data //evita escribir getters y setters
@NoArgsConstructor // Constructor vacío
public class Creature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;

   
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
