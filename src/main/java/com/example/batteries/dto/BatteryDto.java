package com.example.batteries.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryDto {
    private long id;
    private String name;
    private int postcode;
    private double wattCapacity;
}
