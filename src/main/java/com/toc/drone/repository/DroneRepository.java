package com.toc.drone.repository;

import com.toc.drone.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone,Long> {
    Optional<Drone> getDroneBySerial(String serial);
}
