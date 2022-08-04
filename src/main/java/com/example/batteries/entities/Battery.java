package com.example.batteries.entities;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "Battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column (unique = true)
    private String name;

    @NonNull private int postcode;
    @NonNull private double wattCapacity;
}
