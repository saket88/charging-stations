package com.saket.chargingstations.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonAutoDetect
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ChargingStationSummary {


    int totalCount;
    int startedCount;
    int stoppedCount;
}
