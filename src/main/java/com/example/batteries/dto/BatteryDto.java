package com.example.batteries.dto;

import lombok.*;

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
