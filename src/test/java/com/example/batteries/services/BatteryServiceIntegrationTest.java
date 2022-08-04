package com.example.batteries.services;

import com.example.batteries.entities.Battery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BatteryServiceIntegrationTest {
    @Mock
    private BatteryService batteryService;

    @Test
    void getTotalWattCapacityWithinSpecifiedRangeEmpty() {
        double totalWatt = BatteryService.getTotalWattCapacityWithinSpecifiedRange(Collections.emptyList());
        Assertions.assertEquals(0.0, totalWatt);
    }
    @Test
    void getTotalWattCapacityWithinSpecifiedRangeSuccess() {
        Assertions.assertEquals(1204.5, BatteryService.getTotalWattCapacityWithinSpecifiedRange(getSampleBatteries()));
    }

    @Test
    void getAvgWattCapacityWithinSpecifiedRangeEmpty() {
        Assertions.assertEquals(0.0, BatteryService.getAvgWattCapacityWithinSpecifiedRange(0.0, 0));
    }
    @Test
    void getAvgWattCapacityWithinSpecifiedRangeSuccess() {
        Assertions.assertEquals(401.5, BatteryService.getAvgWattCapacityWithinSpecifiedRange(BatteryService.getTotalWattCapacityWithinSpecifiedRange(getSampleBatteries()), 3));
    }

    private List<Battery> getSampleBatteries() {
        return new ArrayList<>(){{
            add(new Battery("BTS100", 665500, 400.5));
            add(new Battery("BTS101", 665502, 401.5));
            add(new Battery("BTS102", 665502, 402.5));
        }};
    }
}
