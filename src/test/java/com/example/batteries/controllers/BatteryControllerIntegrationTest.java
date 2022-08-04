package com.example.batteries.controllers;

import com.example.batteries.dto.BatteriesWithinPostcodeRangeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.dto.PostResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.repositories.BatteryRepository;
import com.example.batteries.services.BatteryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.simple.parser.JSONParser;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BatteryController.class)
class BatteryControllerIntegrationTest {

    private final short PORT = (short) 9090;
    private final String BATTERY_CONTROLLER_ENDPOINT = "/battery";
    private final String BASE_URL = "http://localhost:" + PORT + BATTERY_CONTROLLER_ENDPOINT;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean BatteryService batteryService;
    //@MockBean BatteryRepository batteryRepository;

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBatteryErrZeroBatteries() throws Exception {
        when(batteryService.addBatteryInfo(Collections.emptyList()))
                .thenReturn(new PostResponseDto("No batteries added as empty request for fired !", Collections.emptyList(), (short) 400));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(BASE_URL + "/add-info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addBatteryErrRepeatedBattery() throws Exception {
        when(batteryService.addBatteryInfo(getSampleRepeatedBatteries()))
                .thenReturn(new PostResponseDto(
                        "could not execute statement; SQL [n/a]; constraint [battery.UK_lvm1k696hw3f4elq3831ygeal]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                        Collections.emptyList(),
                        (short) 415));

        mockMvc.perform(post(BASE_URL + "/add-info"))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }


    @Test
    void getBatteryInfoSuccess() throws Exception {
        int postcodeLowVal = 665502;
        int postcodeHighVal = 665505;

        List<Battery> sampleBatteries = getSampleBatteries();
        sampleBatteries.sort(Comparator.comparing(Battery::getName));
        double totalWattCapacity = BatteryService.getTotalWattCapacityWithinSpecifiedRange(sampleBatteries);

        when(batteryService.getBatteriesWithinPostcodeRange(postcodeLowVal, postcodeHighVal))
                .thenReturn(new GetResponseDto(
                        "Batteries within specified postcode range are fetched successfully !",
                        new BatteriesWithinPostcodeRangeDto(sampleBatteries, totalWattCapacity, BatteryService.getAvgWattCapacityWithinSpecifiedRange(totalWattCapacity, sampleBatteries.size())),
                        (short) 200));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(BASE_URL + "/get-info")
                .param("postcodeLowVal", Integer.toString(postcodeLowVal))
                .param("postcodeHighVal", Integer.toString(postcodeHighVal))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getBatteryInfoErrZeroBatteriesInSpecifiedRange() throws Exception {
        int postcodeLowVal = 1020;
        int postcodeHighVal = 1009;

        when(batteryService.getBatteriesWithinPostcodeRange(postcodeLowVal, postcodeHighVal))
                .thenReturn(new GetResponseDto("No batteries found within specified postcode range !", null, (short) 200));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(BASE_URL + "/get-info")
                .param("postcodeLowVal", Integer.toString(postcodeLowVal))
                .param("postcodeHighVal", Integer.toString(postcodeHighVal))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getBatteryInfoErrNoParameterPassed() throws Exception {
        int postcodeLowVal = 0;
        int postcodeHighVal = 0;

        when(batteryService.getBatteriesWithinPostcodeRange(postcodeLowVal, postcodeHighVal))
                .thenReturn(new GetResponseDto("No batteries found within specified postcode range !", null, (short) 400));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(BASE_URL + "/get-info")
                .param("postcodeLowVal", Integer.toString(postcodeLowVal))
                .param("postcodeHighVal", Integer.toString(postcodeHighVal))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    private List<Battery> getSampleBatteries() {
        return new ArrayList<>(){{
            add(new Battery("BT100", 665500, 400.5));
            add(new Battery("BT101", 665501, 401.5));
            add(new Battery("BT102", 665502, 402.5));
            add(new Battery("BT103", 665503, 403.5));
            add(new Battery("BT104", 665504, 404.5));
            add(new Battery("BT105", 665505, 405.5));
            add(new Battery("BT106", 665506, 406.5));
            add(new Battery("BT107", 665507, 407.5));
        }};
    }
    private List<Battery> getSampleRepeatedBatteries() {
        return new ArrayList<>(){{
            add(new Battery("BT108", 665508, 400.5));
            add(new Battery("BT109", 665509, 401.5));
            add(new Battery("BT109", 665509, 409.5));
            add(new Battery("BT110", 665510, 410.5));
        }};
    }
}