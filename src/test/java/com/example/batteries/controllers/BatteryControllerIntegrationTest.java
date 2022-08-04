package com.example.batteries.controllers;

import com.example.batteries.dto.BatteriesWithinPostcodeRangeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.GetResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.mapper.BatteryAndBatteryDtoMapper;
import com.example.batteries.services.BatteryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBatterySuccess() throws Exception {
        List<Battery> b = getSampleBatteries();
        List<BatteryDto> dto = BatteryAndBatteryDtoMapper.batteryToDto(b);

        when(batteryService.addBatteryInfo(b)).thenReturn(b);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/battery/add-info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsBytes(dto));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void addBatteryErrZeroBatteries() throws Exception {
        List<Battery> b = getSampleBatteries();
        List<BatteryDto> dto = BatteryAndBatteryDtoMapper.batteryToDto(b);

        when(batteryService.addBatteryInfo(Collections.emptyList())).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/battery/add-info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsBytes(Collections.emptyList()));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
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
                .thenReturn(new GetResponseDto("No batteries found within specified postcode range !", null, (short) 404));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(BASE_URL + "/get-info")
                .param("postcodeLowVal", Integer.toString(postcodeLowVal))
                .param("postcodeHighVal", Integer.toString(postcodeHighVal))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
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
            add(new Battery("BT110", 665500, 400.5));
            add(new Battery("BT121", 665501, 401.5));
            add(new Battery("BT132", 665502, 402.5));
            add(new Battery("BT143", 665503, 403.5));
            add(new Battery("BT154", 665504, 404.5));
            add(new Battery("BT165", 665505, 405.5));
            add(new Battery("BT176", 665506, 406.5));
            add(new Battery("BT187", 665507, 407.5));
        }};
    }
}