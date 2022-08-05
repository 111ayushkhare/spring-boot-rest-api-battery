package com.example.batteries.controllers;

import com.example.batteries.dto.BatteriesInPostcodeDto;
import com.example.batteries.dto.BatteryDto;
import com.example.batteries.dto.BatteryResponseDto;
import com.example.batteries.entities.Battery;
import com.example.batteries.mapper.BatteryDtoMapper;
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
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBatteryInfoSuccess() throws Exception {
        int postcodeLowVal = 665502;
        int postcodeHighVal = 665505;

        List<Battery> sampleBatteries = getSampleBatteries();
        sampleBatteries.sort(Comparator.comparing(Battery::getName));
        double totalWattCapacity = BatteryService.getTotalWattCapacityWithinSpecifiedRange(sampleBatteries);

        when(batteryService.getBatteriesWithinPostcodeRange(postcodeLowVal, postcodeHighVal))
                .thenReturn(new BatteryResponseDto(
                        "Batteries within specified postcode range are fetched successfully !",
                        new BatteriesInPostcodeDto(sampleBatteries, totalWattCapacity, BatteryService.getAvgWattCapacityWithinSpecifiedRange(totalWattCapacity, sampleBatteries.size())),
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
                .thenReturn(new BatteryResponseDto("No batteries found within specified postcode range !", null, (short) 404));

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
                .thenReturn(new BatteryResponseDto("No batteries found within specified postcode range !", null, (short) 400));

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
    void addBatterySuccess() throws Exception {
        List<Battery> b = getSampleBatteries();

        List<Battery> batteriesWithNullIds = b.stream().map(battery -> Battery.builder()
                        .id(null)
                        .name(battery.getName())
                        .postcode(battery.getPostcode())
                        .wattCapacity(battery.getWattCapacity())
                        .build())
                .collect(Collectors.toList());

        List<BatteryDto> dto = b.stream()
                .map(battery -> BatteryDtoMapper.map(battery))
                .collect(Collectors.toList());

        when(batteryService.addBatteryInfo(batteriesWithNullIds)).thenReturn(b);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/battery/add-info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void addBatteryErrZeroBatteries() throws Exception {

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


    private List<Battery> getSampleBatteries() {
        return new ArrayList<>(){{
            add(new Battery(1l,"BT110", 665500, 400.5));
            add(new Battery(2l,"BT121", 665501, 401.5));
            add(new Battery(3l, "BT132", 665502, 402.5));
            add(new Battery(4l, "BT143", 665503, 403.5));
            add(new Battery(5l,"BT154", 665504, 404.5));
            add(new Battery(6l,"BT165", 665505, 405.5));
            add(new Battery(7l,"BT176", 665506, 406.5));
            add(new Battery(8l,"BT187", 665507, 407.5));
        }};
    }
}