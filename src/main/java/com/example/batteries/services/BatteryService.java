package com.example.batteries.services;

import com.example.batteries.dto.BatteriesWithinPostcodeRangeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    public List<BatteryDto> addBatteryInfo(List<Battery> batteries) {
        List<Battery> savedBatteries = new ArrayList<>();
        batteryRepository.saveAll(batteries).iterator().forEachRemaining(savedBatteries::add);

        List<BatteryDto> createdBatteries = new ArrayList<>();
        for (Battery battery: savedBatteries) {
            createdBatteries.add(new BatteryDto(battery.getId(), battery.getName(), battery.getPostcode(), battery.getWattCapacity()));
        }

        return createdBatteries;
    }

    public BatteriesWithinPostcodeRangeDto getBatteriesWithinPostcodeRange(int postcodeLow, int postcodeHigh) {
        List<Battery> l = batteryRepository.findAllBatteriesWithinSpecifiedPostcodeRange(postcodeLow, postcodeHigh);

        l.sort(Comparator.comparing(Battery::getName));
        double totalWattCapacity = getTotalWattCapacityWithinSpecifiedRange(l);

        return new BatteriesWithinPostcodeRangeDto(l, totalWattCapacity, getAvgWattCapacityWithinSpecifiedRange(totalWattCapacity, l.size()));
    }

    private double getTotalWattCapacityWithinSpecifiedRange(List<Battery> batteries) {
        double totalWattCapacity = 0;
        for (Battery b: batteries) {
            totalWattCapacity += b.getWattCapacity();
        }
        return totalWattCapacity;
    }

    private double getAvgWattCapacityWithinSpecifiedRange(double totalWattCap, int n) {
        return totalWattCap / (double) n;
    }
}
