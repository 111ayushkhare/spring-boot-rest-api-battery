package com.example.batteries.dto;

import lombok.Getter;
import lombok.Setter;

// This response Data Transfer object is for the GET request
// for fetching the batteries within specified postcode range
@Getter
@Setter
public class BatteryResponseDto {
    private String message;
    private BatteriesInPostcodeDto batteriesWithinPostcodeRange;
    private int statusCode;

    public BatteryResponseDto(String message, BatteriesInPostcodeDto batteriesWithinPostcodeRange, short statusCode) {
        this.message = message;
        this.batteriesWithinPostcodeRange = batteriesWithinPostcodeRange;
        this.statusCode = statusCode;
    }
}
