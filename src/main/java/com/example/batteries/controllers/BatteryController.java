package com.example.batteries.controllers;

import com.example.batteries.dto.BatteriesWithinPostcodeRangeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.dto.PostResponseDto;
import com.example.batteries.entities.Battery;
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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<PostResponseDto> addBattery(@RequestBody List<Battery> batteries) {
        return new ResponseEntity<>(batteryService.addBatteryInfo(batteries), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-info", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<GetResponseDto> getBatteryInfo(
        @RequestParam(value = "postcodeLowVal") int postcodeLowerValue,
        @RequestParam(value = "postcodeHighVal") int postcodeHigherValue
    ) {
        return new ResponseEntity<>(batteryService.getBatteriesWithinPostcodeRange(postcodeLowerValue, postcodeHigherValue), new HttpHeaders(), HttpStatus.OK);
    }

}
