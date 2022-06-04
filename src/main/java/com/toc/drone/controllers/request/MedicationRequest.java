package com.toc.drone.controllers.request;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class MedicationRequest {
    private String name;
    private int weight;
    private String code;
    private String imageUrl;
}
