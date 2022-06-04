package com.toc.drone.testing.services;

import com.toc.drone.models.Drone;
import com.toc.drone.models.Model;
import com.toc.drone.models.State;
import com.toc.drone.repository.DroneRepository;
import com.toc.drone.services.DroneService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @MockBean
    private DroneRepository repository;


    @Before
    public void setUp() {
        Drone drone1 = new Drone();
        Drone drone2 = new Drone();
        Drone drone3 = new Drone();

        drone1.setState(State.IDLE);
        drone1.setSerial("HJS_56");
        drone1.setBatteryCapacity(45);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setWeightLimit(120);

        drone2.setState(State.IDLE);
        drone2.setSerial("HJS_57");
        drone2.setBatteryCapacity(100);
        drone2.setModel(Model.HEAVYWEIGHT);
        drone2.setWeightLimit(400);

        drone3.setState(State.IDLE);
        drone3.setSerial("HJS_59");
        drone3.setBatteryCapacity(85);
        drone3.setModel(Model.CRUISERWEIGHT);
        drone3.setWeightLimit(500);


        List<Drone> allDrones = Arrays.asList(drone1, drone2, drone3);

        Mockito.when(repository.getDroneBySerial(drone1.getSerial())).thenReturn(Optional.of(drone1));
        Mockito.when(repository.getDroneBySerial(drone2.getSerial())).thenReturn(Optional.of(drone2));
        Mockito.when(repository.getDroneBySerial("wrongSerial").orElse(null)).thenReturn(null);
        Mockito.when(repository.findById(drone1.getId())).thenReturn(Optional.of(drone1));
        Mockito.when(repository.findAll()).thenReturn(allDrones);
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
    }

    @Test
    public void whenAValidSerial_thenADroneShouldBeFound() {
        String serial = "HJS_56";
        Drone found = droneService.getDroneBySerial(serial).get();
        assertThat(found.getSerial()).isEqualTo(serial);
    }

    @Test
    public void whenAInvalidSerial_thenDroneIsNotFound() {
        String serial = "invalidSerial";
        Optional<Drone> found = droneService.getDroneBySerial(serial);
        assertThat(found).isEmpty();
        verifynFindBySerialIsCallingOnce(serial);
    }

    @Test
    public void given3Drones_whenGetAll_thenReturn3Records() {
        Drone drone1 = new Drone();
        Drone drone2 = new Drone();
        Drone drone3 = new Drone();

        drone1.setState(State.IDLE);
        drone1.setSerial("HJS_56");
        drone1.setBatteryCapacity(45);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setWeightLimit(120);

        drone2.setState(State.IDLE);
        drone2.setSerial("HJS_57");
        drone2.setBatteryCapacity(100);
        drone2.setModel(Model.HEAVYWEIGHT);
        drone2.setWeightLimit(400);

        drone3.setState(State.IDLE);
        drone3.setSerial("HJS_59");
        drone3.setBatteryCapacity(85);
        drone3.setModel(Model.CRUISERWEIGHT);
        drone3.setWeightLimit(500);

        List<Drone> allDrones = droneService.fetchAll();
        verifyFindAllIsCallingOnce();
        assertThat(allDrones).hasSize(3).extracting(Drone::getSerial).contains(drone1.getSerial(), drone2.getSerial(), drone3.getSerial());
    }

    private void verifynFindBySerialIsCallingOnce(String serial) {
        Mockito.verify(repository, VerificationModeFactory.times(1)).getDroneBySerial(serial);
        Mockito.reset(repository);
    }

    private void verifyFindByIdIsCallingOnce() {
        Mockito.verify(repository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(repository);
    }

    private void verifyFindAllIsCallingOnce() {
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(repository);
    }

}
