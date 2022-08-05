package com.example.batteries.mapper;

import com.example.batteries.dto.BatteryDto;
import com.example.batteries.entities.Battery;

public class BatteryDtoMapper {
    public static Battery map(BatteryDto dto){
        return Battery.builder()
                .name(dto.getName())
                .postcode(dto.getPostcode())
                .wattCapacity(dto.getWattCapacity()).build();
    }

    public static BatteryDto map(Battery model){
        return new BatteryDto(model.getId(), model.getName(),model.getPostcode(),model.getWattCapacity());
    }
}
