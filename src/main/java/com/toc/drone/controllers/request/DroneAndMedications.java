package com.toc.drone.controllers.request;

import com.toc.drone.controllers.responses.DroneResponse;
import com.toc.drone.controllers.responses.DroneWithoutList;
import com.toc.drone.controllers.responses.MedicationResponse;
import com.toc.drone.controllers.responses.MedicationWithoutMapping;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class DroneAndMedications {
    private DroneResponse drone;
    private List<MedicationWithoutMapping> medications;
}
