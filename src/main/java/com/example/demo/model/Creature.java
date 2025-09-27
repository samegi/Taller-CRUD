package com.example.demo.model; // #Paquete del proyecto

import jakarta.persistence.*;     // JPA (para mapear a la base de datos)
import lombok.Data;             // Lombok (genera getters/setters/toString)
import lombok.NoArgsConstructor; // Lombok (constructor vacío)

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

    // Relación con Zone (cuando crees la entidad Zone)
    // @ManyToOne
    // @JoinColumn(name = "zone_id")
    // private Zone zone;
}
