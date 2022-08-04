package com.example.batteries.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private String message;
    private List<BatteryDto> batteries;
    private int statusCode;

    public PostResponseDto(String message, List<BatteryDto> batteries, short statusCode) {
        this.message = message;
        this.batteries = batteries;
        this.statusCode = statusCode;
    }
}
