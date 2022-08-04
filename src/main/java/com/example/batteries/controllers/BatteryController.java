package com.example.batteries.controllers;

import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.mapper.BatteryAndBatteryDtoMapper;
import com.example.batteries.services.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battery")
public class BatteryController {

    @Autowired
    BatteryService batteryService;

    @PostMapping(value = "/add-info", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BatteryDto>> addBattery(@RequestBody List<BatteryDto> batteries) {
        try {
            List<Battery> batteryList = BatteryAndBatteryDtoMapper.dtoToBattery(batteries);
            List<Battery> savedBatteries = batteryService.addBatteryInfo(batteryList);

            if (savedBatteries == null) {
                throw new Exception();
            }
            return new ResponseEntity<>(BatteryAndBatteryDtoMapper.batteryToDto(savedBatteries), new HttpHeaders(), savedBatteries.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @GetMapping(value = "/get-info", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<GetResponseDto> getBatteryInfo(
        @RequestParam(value = "postcodeLowVal", defaultValue = "0") int postcodeLowerValue,
        @RequestParam(value = "postcodeHighVal", defaultValue = "0") int postcodeHigherValue
    ) {
        GetResponseDto getResponseDto = batteryService.getBatteriesWithinPostcodeRange(postcodeLowerValue, postcodeHigherValue);
        return new ResponseEntity<>(getResponseDto, new HttpHeaders(), getResponseDto.getStatusCode());
    }

}
