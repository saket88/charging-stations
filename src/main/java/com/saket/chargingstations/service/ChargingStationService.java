package com.saket.chargingstations.service;

import com.saket.chargingstations.model.ChargingSession;
import com.saket.chargingstations.model.ChargingStationSummary;
import com.saket.chargingstations.model.StatusEnum;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChargingStationService {

    ConcurrentHashMap<UUID,ChargingSession> chargingSessionConcurrentHashMap= new ConcurrentHashMap<>();
    public ChargingSession startChargingSession(ChargingSession chargingSession) {
        return  chargingSessionConcurrentHashMap.putIfAbsent(chargingSession.getId(),chargingSession);
    }


    public ChargingSession updateChargingSession(UUID uuid) {
        ChargingSession chargingSession= chargingSessionConcurrentHashMap.get(uuid);
        chargingSession.finished();
        chargingSessionConcurrentHashMap.putIfAbsent(chargingSession.getId(),chargingSession);
        return chargingSession;
    }

    public ArrayList<ChargingSession> findAll() {
       return (ArrayList<ChargingSession>) chargingSessionConcurrentHashMap.entrySet()
               .stream()
               .map(uuidChargingSessionEntry -> uuidChargingSessionEntry.getValue())
               .collect(Collectors.toList());
    }

    public ChargingStationSummary findSummary() {
        ArrayList<ChargingSession> totalSessions = findAll();
        int totalCount=totalSessions.size();
        int stoppedCount = getStoppedCount(totalSessions);
        int startedCount=totalCount-stoppedCount;
        return new ChargingStationSummary(totalCount,startedCount,stoppedCount);
    }

    private int getStoppedCount(ArrayList<ChargingSession> totalSessions) {
        return (int) totalSessions.stream()
                    .filter(chargingSession -> chargingSession.getStatus().equals(StatusEnum.FINISHED))
                    .count();
    }
}
