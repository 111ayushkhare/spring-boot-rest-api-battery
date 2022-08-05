package com.example.batteries.dto;

import com.example.batteries.entities.Battery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatteriesInPostcodeDto {
    private List<Battery> batteries;
    private double totalWattCapacity, avgWattCapacity;
}
