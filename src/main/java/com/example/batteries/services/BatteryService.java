package com.example.batteries.services;

import com.example.batteries.dto.BatteriesWithinPostcodeRangeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.dto.PostResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    private Logger logs = Logger.getLogger(BatteryService.class.getName());

    @CachePut(cacheNames = "cache_battery", key = "#batteries")
    public PostResponseDto addBatteryInfo(List<Battery> batteries) {
        try {
            List<Battery> savedBatteries = new ArrayList<>();
            batteryRepository.saveAll(batteries).iterator().forEachRemaining(savedBatteries::add);

            List<BatteryDto> createdBatteries = new ArrayList<>();
            for (Battery battery: savedBatteries) {
                createdBatteries.add(new BatteryDto(battery.getId(), battery.getName(), battery.getPostcode(), battery.getWattCapacity()));
            }

            logs.info("Batteries are added in the MYSQL database...");
            return new PostResponseDto("Batteries added successfully !", createdBatteries, (short) 201);
        } catch (Exception e) {
            logs.info(e.getMessage());
            return new PostResponseDto(e.getMessage(), Collections.emptyList(), (short) 400);
        }
    }

    @Cacheable(cacheNames = "cache_battery", key = "{#postcodeLow, #postcodeHigh}")
    public GetResponseDto getBatteriesWithinPostcodeRange(int postcodeLow, int postcodeHigh) {
        try {
            List<Battery> l = batteryRepository.findAllBatteriesWithinSpecifiedPostcodeRange(postcodeLow, postcodeHigh);

            l.sort(Comparator.comparing(Battery::getName));
            double totalWattCapacity = getTotalWattCapacityWithinSpecifiedRange(l);

            logs.info("Batteries within specified postcode range are fetched...");
            return new GetResponseDto("Batteries within specified postcode range are fetched successfully !", new BatteriesWithinPostcodeRangeDto(l, totalWattCapacity, getAvgWattCapacityWithinSpecifiedRange(totalWattCapacity, l.size())), (short) 200);
        } catch (Exception e) {
            logs.info(e.getMessage());
            return new GetResponseDto(e.getMessage(), null, (short) 404);
        }
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
