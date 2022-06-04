package com.toc.drone.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="medication")
@Getter
@Setter
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "weight")
    private int weight;

    @NotNull
    @Column(name = "code")
    private String code;

    @Column(name = "image_url")
    private String imageUrl;

    @JoinColumn(name="id_drone")
    @ManyToOne(optional = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Drone drone;



}
