package com.toc.drone.controllers.responses;
import lombok.*;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class MedicationResponse {
    private long id;
    private String name;
    private int weight;
    private String code;
    private String imageUrl;
    private DroneWithoutList drone;
}
