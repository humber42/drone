package com.toc.drone.controllers.responses;

import com.toc.drone.models.Model;
import com.toc.drone.models.State;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class DroneWithoutList {
    private long id;
    private String serial;
    private Model model;
    private int weightLimit;
    private int batteryCapacity;
    private State state;
}
