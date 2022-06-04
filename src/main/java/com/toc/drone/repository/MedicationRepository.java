package com.toc.drone.repository;

import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long> {
    List<Medication>getAllByDrone(Drone drone);
    List<Medication>getAllByDrone_Id(long id);
    List<Medication>getAllByDrone_Serial(String serial);
    Optional<Medication> getMedicationByCode(String code);
    void deleteByCode(String code);
}
