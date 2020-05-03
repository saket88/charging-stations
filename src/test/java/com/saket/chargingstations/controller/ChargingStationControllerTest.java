package com.saket.chargingstations.controller;

import com.saket.chargingstations.model.ChargingSession;
import com.saket.chargingstations.model.ChargingStationSummary;
import com.saket.chargingstations.model.StatusEnum;
import com.saket.chargingstations.service.ChargingStationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ChargingStationController.class)
public class ChargingStationControllerTest {
    private static final String BASE_URI = "/chargingSessions";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChargingStationService chargingStationService;

    @Test
    public void shouldBeAbleToPostChargingSessions() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        ChargingSession chargingSession =
                ChargingSession
                        .builder()
                        .id(uuid)
                        .stationId("1")
                        .status(StatusEnum.IN_PROGRESS)
                         .startedAt(now)
                          .build();

        when(chargingStationService.startChargingSession(ArgumentMatchers.any(ChargingSession.class))).thenReturn(chargingSession);

        mockMvc.perform(post(BASE_URI).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content("{\"stationId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.startedAt", is(now.toString())))
                .andExpect(jsonPath("$.id", is(uuid.toString())));

    }

    @Test
    public void shouldBThrowBadRequestIfMorethanOneOneParameter() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        ChargingSession chargingSession =
                ChargingSession
                        .builder()
                        .id(uuid)
                        .stationId("1")
                        .status(StatusEnum.IN_PROGRESS)
                        .startedAt(now)
                        .build();


        mockMvc.perform(post(BASE_URI).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(chargingSession)))
                .andExpect(status().isBadRequest());

    }
    @Test(expected = NestedServletException.class)
    public void shouldBThrowBadRequestIfStationIdIsNull() throws Exception {
        mockMvc.perform(post(BASE_URI).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content("{\"stationId\":\"\"}"));

    }

    @Test
    public void shouldBeAbleToUpdateChargingSessions() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        ChargingSession chargingSession =
                ChargingSession
                        .builder()
                        .id(uuid)
                        .stationId("1")
                        .status(StatusEnum.FINISHED)
                        .startedAt(now)
                        .build();

        when(chargingStationService.updateChargingSession(ArgumentMatchers.any())).thenReturn(chargingSession);

        mockMvc.perform(put(BASE_URI+"/"+uuid.toString()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("FINISHED")))
                .andExpect(jsonPath("$.startedAt", is(now.toString())))
                .andExpect(jsonPath("$.id", is(uuid.toString())));

    }





    @Test
    public void shouldBeAbleToGetTheSummary() throws Exception {
        ChargingStationSummary chargingStationSummary= ChargingStationSummary.builder()
                .startedCount(3)
                .stoppedCount(2)
                .totalCount(5)
                .build();
        when(chargingStationService.findSummary()).thenReturn(chargingStationSummary);

        mockMvc.perform(get(BASE_URI+"/summary").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.startedCount", is(3)))
                .andExpect(jsonPath("$.stoppedCount", is(2)))
                .andExpect(jsonPath("$.totalCount", is(5)));

    }
}
