package com.toc.drone.controllers.responses;

import com.toc.drone.models.Medication;
import com.toc.drone.models.Model;
import com.toc.drone.models.State;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class DroneResponse {
    private long id;
    private String serial;
    private Model model;
    private int weightLimit;
    private int batteryCapacity;
    private State state;
    private List<MedicationWithoutMapping> medications;
}
