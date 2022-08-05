package com.example.batteries.services;

import com.example.batteries.dto.BatteriesInPostcodeDto;
import com.example.batteries.dto.BatteryResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    private Logger logs = Logger.getLogger(BatteryService.class.getName());

    /**
     * This method communicates with batteryRepository and
     * adds the list of batteries to the database
     *
     * @param batteries (list of Battery objects)
     * @return Batteries saved to the database or null in error
     */
    @CachePut(cacheNames = "cache_battery", key = "#batteries")
    public List<Battery> addBatteryInfo(List<Battery> batteries) {
        try {
            List<Battery> savedBatteries = batteryRepository.saveAll(batteries);

            if (savedBatteries.isEmpty()) {
                logs.info("No batteries added as empty request for fired...");
            } else {
                logs.info("Batteries are added in the MYSQL database...");
            }

            return savedBatteries;
        } catch (Exception e) {
            logs.info(e.getMessage());
            return null;
        }
    }

    /**
     * This method communicates with batteryRepository and
     * retrieves list of batteries within specified postcode range
     *
     * @param postcodeLow
     * @param postcodeHigh
     * @return list of batteries within specified postcode range
     */
    @Cacheable(cacheNames = "cache_battery", key = "{#postcodeLow, #postcodeHigh}")
    public BatteryResponseDto getBatteriesWithinPostcodeRange(int postcodeLow, int postcodeHigh) {
        try {
            List<Battery> l = batteryRepository.findAllBatteriesWithinSpecifiedPostcodeRange(postcodeLow, postcodeHigh);

            if (l.isEmpty()) {
                logs.info("No batteries found within specified postcode range...");
                return new BatteryResponseDto("No batteries found within specified postcode range !", null, (short) 404);
            } else {
                l.sort(Comparator.comparing(Battery::getName));
                double totalWattCapacity = getTotalWattCapacityWithinSpecifiedRange(l);

                logs.info("Batteries within specified postcode range are fetched...");
                return new BatteryResponseDto("Batteries within specified postcode range are fetched successfully !", new BatteriesInPostcodeDto(l, totalWattCapacity, getAvgWattCapacityWithinSpecifiedRange(totalWattCapacity, l.size())), (short) 200);
            }

        } catch (Exception e) {
            logs.info(e.getMessage());
            return new BatteryResponseDto(e.getMessage(), null, (short) 404);
        }
    }

    // This method gets total watt capacity for the supplied list of batteries
    public static double getTotalWattCapacityWithinSpecifiedRange(List<Battery> batteries) {
        double totalWattCapacity = 0;
        for (Battery b: batteries) {
            totalWattCapacity += b.getWattCapacity();
        }
        return totalWattCapacity;
    }

    // This method gets average watt capacity for the supplied list of batteries
    public static double getAvgWattCapacityWithinSpecifiedRange(double totalWattCap, int n) {
        return n == 0 ? (double) 0 : totalWattCap / (double) n;
    }
}
