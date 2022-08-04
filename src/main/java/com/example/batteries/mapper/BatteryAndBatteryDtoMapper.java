package com.example.batteries.mapper;

import com.example.batteries.dto.BatteryDto;
import com.example.batteries.entities.Battery;

import java.util.ArrayList;
import java.util.List;

public class BatteryAndBatteryDtoMapper {

    /**
     * This method converts BatteryDto object to Battery object
     *
     * @param batteryDto (List of BatteryDto objects)
     * @return List of Battery objects
     */
    public static List<Battery> dtoToBattery(List<BatteryDto> batteryDto) {
        List<Battery> l = new ArrayList<>();
        for (BatteryDto dto : batteryDto) {
            l.add(new Battery(dto.getId(), dto.getName(), dto.getPostcode(), dto.getWattCapacity()));
        }
        return l;
    }

    /**
     * This method converts Battery object to BatteryDto object
     *
     * @param batteryList
     * @return List of BatteryDto objects
     */
    public static List<BatteryDto> batteryToDto(List<Battery> batteryList) {
        List<BatteryDto> l = new ArrayList<>();
        for (Battery battery: batteryList) {
            l.add(new BatteryDto(battery.getId(), battery.getName(), battery.getPostcode(), battery.getWattCapacity()));
        }
        return l;
    }
}
