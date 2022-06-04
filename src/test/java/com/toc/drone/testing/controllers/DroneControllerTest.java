package com.toc.drone.testing.controllers;

import com.toc.drone.DroneApplication;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Model;
import com.toc.drone.models.State;
import com.toc.drone.repository.DroneRepository;
import com.toc.drone.testing.JsonUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DroneApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DroneRepository droneRepository;

    @After
    public void resetDb() {
        droneRepository.deleteAll();
    }

    @Test
    public void whenAValidInput_CreateADrone() throws IOException, Exception {
        Drone drone1 = new Drone();
        drone1.setState(State.IDLE);
        drone1.setSerial("HJS_56");
        drone1.setBatteryCapacity(45);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setWeightLimit(120);

        mockMvc.perform(post("/api/v1/drone/register").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(drone1)));

        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).extracting(Drone::getSerial).containsOnly("HJS_56");
    }

    @Test
    public void givenDrones_whenGetDrones_thenResult200() throws Exception {
        Drone drone2 = new Drone();
        Drone drone3 = new Drone();

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

        this.createTestDrone(drone2);
        this.createTestDrone(drone3);

        mockMvc.perform(get("/api/v1/drone/all").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].serial", is("HJS_57")))
                .andExpect(jsonPath("$[1].serial", is(drone3.getSerial())));
    }

    @Test
    public void sendDroneId_whenGetADrone_thenResult200() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.IDLE);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);

        mockMvc.perform(get("/api/v1/drone/id/" + savedDrone.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.serial", is(drone.getSerial())))
        ;
    }

    @Test
    public void sendDroneId_thenResult200AndGetLevelBatery() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.IDLE);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(get("/api/v1/drone/check-battery/id/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(drone.getBatteryCapacity())));
    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToLoading() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.IDLE);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/loading/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToIdle() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.RETURNING);
        drone.setSerial("XIAO_54_97");
        drone.setBatteryCapacity(100);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/idle/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToLoaded() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/loaded/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToDelivering() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/delivering/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToDelivered() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/delivered/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void sendDroneId_thenChangeTheDroneStatusToReturning() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone savedDrone = this.createTestDrone(drone);
        mockMvc.perform(put("/api/v1/drone/change-to/returning/" + savedDrone.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void updateADrone_ThenStatus200AndADrone() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone saved = this.createTestDrone(drone);
        saved.setSerial("47_HCS");
        saved.setWeightLimit(300);
        saved.setBatteryCapacity(63);

        mockMvc.perform(put("/api/v1/drone/update").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(saved)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void passADroneId_thenDeleteDrone() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.LOADING);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setWeightLimit(400);

        Drone saved = this.createTestDrone(drone);
        mockMvc.perform(delete("/api/v1/drone/delete/" + saved.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void sendDrones_andReceivedAllAvailable_thenResult200() throws Exception {
        Drone drone = new Drone();
        drone.setState(State.IDLE);
        drone.setSerial("XIAO_54_98");
        drone.setBatteryCapacity(69);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setWeightLimit(100);

        Drone drone2 = new Drone();
        drone2.setState(State.LOADED);
        drone2.setSerial("XIAO_54_97");
        drone2.setBatteryCapacity(100);
        drone2.setModel(Model.HEAVYWEIGHT);
        drone2.setWeightLimit(400);

        Drone drone3 = new Drone();
        drone3.setState(State.LOADING);
        drone3.setSerial("XIAO_54_97");
        drone3.setBatteryCapacity(100);
        drone3.setModel(Model.CRUISERWEIGHT);
        drone3.setWeightLimit(500);

        this.createTestDrone(drone);
        this.createTestDrone(drone2);
        this.createTestDrone(drone3);

        mockMvc.perform(get("/api/v1/drone/available-drones").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }


    private Drone createTestDrone(Drone drone) {
        return droneRepository.saveAndFlush(drone);
    }

}
