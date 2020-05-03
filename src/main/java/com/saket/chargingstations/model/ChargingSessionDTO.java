package com.saket.chargingstations.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@JsonAutoDetect
@Getter
public class ChargingSessionDTO {

    @JsonSetter
    String stationId;
}
