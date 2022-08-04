package com.example.batteries.controllers;

import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.dto.PostResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.services.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<PostResponseDto> addBattery(@RequestBody List<Battery> batteries) {
        PostResponseDto postResponseDto = batteryService.addBatteryInfo(batteries);
        return new ResponseEntity<>(postResponseDto, new HttpHeaders(), postResponseDto.getStatusCode());
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
