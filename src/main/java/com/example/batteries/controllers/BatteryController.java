package com.example.batteries.controllers;

import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.BatteryResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.mapper.BatteryDtoMapper;
import com.example.batteries.services.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/battery")
public class BatteryController {

    @Autowired
    BatteryService batteryService;

    /**
     * This method is the endpoint for adding list of batteries
     * Request body is in JSON format
     * Returns JSON response
     *
     * @param batteries
     * @return ResponseEntity with BatteryDto as response part
     */
    @PostMapping(value = "/add-info", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BatteryDto>> addBattery(@RequestBody List<BatteryDto> batteries) {

        try {
            // Start: This conversion can be moved to business logic
            List<Battery> batteryList = batteries.stream()
                    .map(batteryDto -> BatteryDtoMapper.map(batteryDto))
                    .collect(Collectors.toList());
            // End

            List<Battery> savedBatteries = batteryService.addBatteryInfo(batteryList);

            // Start: This conversion can be moved to business logic
            List<BatteryDto> batteryDto = savedBatteries.stream()
                    .map(battery -> BatteryDtoMapper.map(battery))
                    .collect(Collectors.toList());
            // End

            if (savedBatteries == null) {
                throw new Exception();
            }
            return new ResponseEntity<>(batteryDto, new HttpHeaders(), savedBatteries.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    /**
     * This method is the endpoint to get list of batteries within specified postcode range
     * Returns JSON response
     *
     * @param postcodeLowerValue
     * @param postcodeHigherValue
     * @return list of batteries within specified postcode range
     */
    @GetMapping(value = "/get-info", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BatteryResponseDto> getBatteryInfo(
        @RequestParam(value = "postcodeLowVal", defaultValue = "0") int postcodeLowerValue,
        @RequestParam(value = "postcodeHighVal", defaultValue = "0") int postcodeHigherValue
    ) {
        BatteryResponseDto getResponseDto = batteryService.getBatteriesWithinPostcodeRange(postcodeLowerValue, postcodeHigherValue);
        return new ResponseEntity<>(getResponseDto, new HttpHeaders(), getResponseDto.getStatusCode());
    }

}
