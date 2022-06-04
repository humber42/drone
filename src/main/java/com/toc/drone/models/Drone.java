package com.toc.drone.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "drone")
@Getter
@Setter
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Serial must be not null")
    @Column(name="serial",length = 100)
    private String serial;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private Model model;


    @NotNull
    @Max(value = 500, message = "The weight limit of a drone is 500gr")
    @Column(name = "weight_limit")
    private int weightLimit;

    @NotNull
    @Max(value = 100, message = "The maximum capacity of a battery is 100%")
    @Column(name ="battery_capacity")
    private int batteryCapacity;


    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "drone")
    private List<Medication> medications;

}
