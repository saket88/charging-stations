package com.saket.chargingstations.controller;

import com.saket.chargingstations.model.ChargingSession;
import com.saket.chargingstations.model.ChargingSessionDTO;
import com.saket.chargingstations.model.ChargingStationSummary;
import com.saket.chargingstations.service.ChargingStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class ChargingStationController {

    @Autowired
    ChargingStationService chargingStationService;

    @PostMapping(value = "/chargingSessions")
    public ResponseEntity<ChargingSession> startChargingSession(@RequestBody ChargingSessionDTO chargingSessionDTO){
        if (chargingSessionDTO.getStationId().equals(""))
            throw new IllegalArgumentException("Please provide Station Id");
        ChargingSession chargingSession= new ChargingSession(chargingSessionDTO);
        chargingStationService.startChargingSession(chargingSession);
        return ResponseEntity
                .created(URI.create("/chargingSessions"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(chargingSession);
    }

    @GetMapping(value = "/chargingSessions")
    public ResponseEntity<List<ChargingSession>> findAll(){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chargingStationService.findAll());
    }

    @GetMapping(value = "/chargingSessions/summary")
    public ResponseEntity<ChargingStationSummary> findSummary(){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chargingStationService.findSummary());
    }

    @PutMapping(value = "/chargingSessions/{id}")
    public ResponseEntity<ChargingSession> updateCharging(@PathVariable String id){
        return ResponseEntity
                .created(URI.create("/chargingSessions"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(chargingStationService.updateChargingSession(UUID.fromString(id)));
    }
}
