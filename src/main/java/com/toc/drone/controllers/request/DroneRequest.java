package com.toc.drone.controllers.request;

import com.toc.drone.models.Model;
import lombok.*;

import javax.validation.constraints.Max;


@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
@ToString@EqualsAndHashCode
public class DroneRequest {
    private String serial;
    private Model model;
    @Max(value = 500,message = "The max of weight is 500gr")
    private int weightLimit;
    private int batteryCapacity;
}
