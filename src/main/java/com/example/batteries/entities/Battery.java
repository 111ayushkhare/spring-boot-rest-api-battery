package com.example.batteries.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @NonNull
    @Column (unique = true)
    private String name;

    @NonNull private int postcode;
    @NonNull private double wattCapacity;
}
