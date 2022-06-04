package com.toc.drone.testing.repository;


import com.toc.drone.models.Drone;
import com.toc.drone.models.Model;
import com.toc.drone.models.State;
import com.toc.drone.repository.DroneRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DroneRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DroneRepository droneRepository;

    @Test
    public void whenFindBySerial_thenReturnDrone() {
        Drone drone1 = new Drone();
        drone1.setState(State.IDLE);
        drone1.setSerial("HJS_56");
        drone1.setBatteryCapacity(45);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setWeightLimit(120);

        entityManager.persistAndFlush(drone1);

        Drone droneFound = droneRepository.getDroneBySerial(drone1.getSerial()).get();
        assertThat(droneFound.getSerial()).isEqualTo(drone1.getSerial());
    }

    @Test
    public void whenInvalidSerial_thenReturnNull() {
        Drone fromDb = droneRepository.getDroneBySerial("doesNotExist").orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindById_thenReturnADrone() {
        Drone drone = new Drone();
        drone.setState(State.IDLE);
        drone.setSerial("HJS_57");
        drone.setBatteryCapacity(100);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);
        entityManager.persistAndFlush(drone);

        Drone fromDb = droneRepository.findById(drone.getId()).orElse(null);
        assertThat(fromDb.getSerial()).isEqualTo(drone.getSerial());
    }

    @Test
    public void whenFindById_thenReturnNull() {
        Drone fromDb = droneRepository.findById(Integer.toUnsignedLong(-111)).orElse(null);
        assertThat(fromDb).isNull();
    }
}
