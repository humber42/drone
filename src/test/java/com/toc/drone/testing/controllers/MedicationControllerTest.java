package com.toc.drone.testing.controllers;

import com.toc.drone.DroneApplication;
import com.toc.drone.models.Medication;
import com.toc.drone.repository.MedicationRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DroneApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MedicationRepository repository;

    @After
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenAValidInput_CreateAMedication() throws Exception {
        Medication medication1 = new Medication();

        medication1.setName("Dipirona");
        medication1.setCode("DIP_76");
        medication1.setImageUrl("localhost:9090/img/3");
        medication1.setWeight(45);

        mockMvc.perform(post("/api/v1/medication/register").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(medication1)))
                .andDo(print()).andExpect(status().isOk());
        List<Medication> medicationList = repository.findAll();
        assertThat(medicationList).extracting(Medication::getCode).containsOnly("DIP_76");
    }

    @Test
    public void givenMedications_whenGetMedications_thenResult200() throws Exception {
        Medication medication1 = new Medication();
        medication1.setName("Dipirona");
        medication1.setCode("DIP_76");
        medication1.setImageUrl("localhost:9090/img/3");
        medication1.setWeight(45);

        Medication medication2 = new Medication();
        medication2.setName("Dipirona");
        medication2.setCode("DIP_76");
        medication2.setImageUrl("localhost:9090/img/3");
        medication2.setWeight(45);

        this.createMedication(medication1);
        this.createMedication(medication2);

        mockMvc.perform(get("/api/v1/medication/all").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    private Medication createMedication(Medication medication) {
        return repository.saveAndFlush(medication);
    }
}
