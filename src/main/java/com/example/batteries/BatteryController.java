package com.example.batteries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
public class BatteryController {

    @Autowired
    BatteryRepository batteryRepository;

    @PostMapping("/battery-add-info")
    public String addBatteryInfo(
        @RequestParam(value = "name") String batteryName,
        @RequestParam(value = "postcode") int batteryPostcode,
        @RequestParam(value = "wattCapacity") int batteryWattCapacity
    ) {
        try {
            batteryRepository.save(new Battery(batteryName, batteryPostcode, batteryWattCapacity));
            return "Battery added successfully!";
        } catch (Exception e) {
            return "Error in adding battery :: " + e;
        }
    }

    @GetMapping("/battery-get-info")
    public List<Battery> getBatteryInfo(
        @RequestParam(value = "postcodeLowVal") int postcodeLowerValue,
        @RequestParam(value = "postcodeHighVal") int postcodeHigherValue
    ) {
        List<Battery> l = batteryRepository.findAllBatteriesWithinSpecifiedPostcodeRange(postcodeLowerValue, postcodeHigherValue);

        l.sort(Comparator.comparing(Battery::getName));

        /*
        Collections.sort(l, new Comparator<Battery>() {
            @Override
            public int compare(Battery b1, Battery b2) {
                return b1.getName().compareTo(b2.getName());
            }
        });
        */

        System.out.println(l);

        return l;
    }

}
