package com.saket.chargingstations.service;

import com.saket.chargingstations.model.ChargingSession;
import com.saket.chargingstations.model.ChargingStationSummary;
import com.saket.chargingstations.model.StatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ChargingStationServiceTest {


    @Spy
    ConcurrentHashMap<UUID, Object> chargingSessionConcurrentHashMap;

    @InjectMocks
    private ChargingStationService chargingStationService;

    @Test
    public void shouldStoreValuesInMap(){
        ChargingSession chargingSession= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now())
                .stoppedAt(LocalDateTime.MIN)
                .status(StatusEnum.IN_PROGRESS)
                .stationId("1")
                .build();


        chargingStationService.startChargingSession(chargingSession);

       verify(chargingSessionConcurrentHashMap,times(1)).putIfAbsent(chargingSession.getId(),chargingSession);


    }


    @Test
    public void shouldUpdateValues(){
        ChargingSession chargingSession= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now())
                .stoppedAt(LocalDateTime.MIN)
                .status(StatusEnum.IN_PROGRESS)
                .stationId("1")
                .build();


        given(chargingSessionConcurrentHashMap.get(chargingSession.getId())).willReturn(chargingSession);

        chargingStationService.updateChargingSession(chargingSession.getId());

        ChargingSession chargingSession1= (ChargingSession) chargingSessionConcurrentHashMap.get(chargingSession.getId());
        verify(chargingSessionConcurrentHashMap,times(1)).putIfAbsent(chargingSession.getId(),chargingSession);
        assertThat(chargingSession1.getStatus(),is(StatusEnum.FINISHED));
        assertThat(chargingSession1.getStoppedAt(),is(not(LocalDateTime.MIN)));


    }

    @Test
    public void shouldFindAll(){
        ChargingSession chargingSession1= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now())
                .stoppedAt(LocalDateTime.MIN)
                .status(StatusEnum.IN_PROGRESS)
                .stationId("1")
                .build();
        ChargingSession chargingSession2= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now().minus(1,ChronoUnit.DAYS))
                .stoppedAt(LocalDateTime.now())
                .status(StatusEnum.FINISHED)
                .stationId("2")
                .build();
        ArrayList<ChargingSession> listOfStations= new ArrayList<>();
        listOfStations.add(chargingSession1);
        listOfStations.add(chargingSession2);
        given(chargingSessionConcurrentHashMap.put(chargingSession1.getId(),chargingSession1)).willCallRealMethod();
        given(chargingSessionConcurrentHashMap.put(chargingSession2.getId(),chargingSession2)).willCallRealMethod();

        ArrayList<ChargingSession> listOfStationsExpected = chargingStationService.findAll();

        assertTrue(listOfStations.equals(listOfStationsExpected));





    }
    @Test
    public void shouldFindSummary(){
        ChargingSession chargingSession1= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now())
                .stoppedAt(LocalDateTime.MIN)
                .status(StatusEnum.IN_PROGRESS)
                .stationId("1")
                .build();
        ChargingSession chargingSession2= ChargingSession.builder()
                .id(UUID.randomUUID())
                .startedAt(LocalDateTime.now().minus(1,ChronoUnit.DAYS))
                .stoppedAt(LocalDateTime.now())
                .status(StatusEnum.FINISHED)
                .stationId("2")
                .build();
        ArrayList<ChargingSession> listOfStations= new ArrayList<>();
        listOfStations.add(chargingSession1);
        listOfStations.add(chargingSession2);
        given(chargingSessionConcurrentHashMap.put(chargingSession1.getId(),chargingSession1)).willCallRealMethod();
        given(chargingSessionConcurrentHashMap.put(chargingSession2.getId(),chargingSession2)).willCallRealMethod();

       ChargingStationSummary chargingStationSummary = chargingStationService.findSummary();

       assertThat(chargingStationSummary.getStartedCount(),is(1));
        assertThat(chargingStationSummary.getStoppedCount(),is(1));
        assertThat(chargingStationSummary.getTotalCount(),is(2));





    }
}
