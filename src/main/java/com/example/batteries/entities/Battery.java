package com.example.batteries.entities;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;

// Using 'lombok' Annotation to implicitly generate Constructors, Getters & Setters
@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "Battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column (unique = true)
    private String name;

    @NonNull private int postcode;
    @NonNull private double wattCapacity;
}
