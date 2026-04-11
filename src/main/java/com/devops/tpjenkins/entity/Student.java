package com.devops.tpjenkins.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Column(nullable = false)
    private String prenom;

    @Min(value = 1, message = "L'âge doit être positif")
    private int age;

    @Column(unique = true, nullable = false)
    private String email;
}
