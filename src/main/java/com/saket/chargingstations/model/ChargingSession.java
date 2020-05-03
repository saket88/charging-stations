package com.saket.chargingstations.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ChargingSession {



    private UUID id = UUID.randomUUID();
    @JsonSetter
    String stationId;
    private LocalDateTime startedAt =LocalDateTime.now();
    private LocalDateTime stoppedAt =LocalDateTime.MIN;
    private StatusEnum status = StatusEnum.IN_PROGRESS;

    public ChargingSession(ChargingSessionDTO chargingSessionDTO) {
        this.stationId=chargingSessionDTO.getStationId();
    }


    @JsonIgnore
    public void finished() {
        this.status=StatusEnum.FINISHED;
        this.stoppedAt=LocalDateTime.now();
    }
}
