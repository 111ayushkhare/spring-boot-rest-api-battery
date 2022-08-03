package com.example.batteries.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetResponseDto {
    private String message;
    private BatteriesWithinPostcodeRangeDto batteriesWithinPostcodeRange;
    private int statusCode;

    public GetResponseDto(String message, BatteriesWithinPostcodeRangeDto batteriesWithinPostcodeRange, short statusCode) {
        this.message = message;
        this.batteriesWithinPostcodeRange = batteriesWithinPostcodeRange;
        this.statusCode = statusCode;
    }
}
