package com.toc.drone.testing.services;

import com.toc.drone.exceptions.InvalidCodeFormatException;
import com.toc.drone.exceptions.NotFoundItemException;
import com.toc.drone.models.Medication;
import com.toc.drone.repository.MedicationRepository;
import com.toc.drone.services.MedicationService;
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
public class MedicationServiceTest {

    @Autowired
    private MedicationService service;

    @MockBean
    private MedicationRepository repository;

    @Before
    public void setUp() {
        Medication medication1 = new Medication();
        Medication medication2 = new Medication();
        Medication medication3 = new Medication();

        medication1.setName("Dipirona");
        medication1.setCode("DIP_76");
        medication1.setImageUrl("localhost:9090/img/3");
        medication1.setWeight(45);

        medication2.setName("Ibuprofeno");
        medication2.setCode("IBP_86");
        medication2.setImageUrl("localhost:9090/img/5");
        medication2.setWeight(86);

        medication3.setName("Karbamazepina");
        medication3.setCode("KAR_12");
        medication3.setImageUrl("localhost:9090/img/45");
        medication3.setWeight(12);

        Mockito.when(repository.saveAndFlush(medication1)).thenReturn(medication1);
        Mockito.when(repository.saveAndFlush(medication2)).thenReturn(medication2);
        Mockito.when(repository.saveAndFlush(medication3)).thenReturn(medication3);

        List<Medication> allMedications = Arrays.asList(medication1, medication2, medication3);

        Mockito.when(repository.getMedicationByCode(medication1.getCode())).thenReturn(Optional.of(medication1));
        Mockito.when(repository.getMedicationByCode(medication2.getCode())).thenReturn(Optional.of(medication2));
        Mockito.when(repository.getMedicationByCode(medication3.getCode())).thenReturn(Optional.of(medication3));
        Mockito.when(repository.getMedicationByCode("WRONG").orElse(null)).thenReturn(null);
        Mockito.when(repository.findById(medication1.getId())).thenReturn(Optional.of(medication1));
        Mockito.when(repository.findAll()).thenReturn(allMedications);
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
    }

    @Test
    public void whenAValidCode_thenReturnShouldBeFound() throws NotFoundItemException, InvalidCodeFormatException {
        String code = "KAR_12";
        Medication found = service.getMedicationByCode(code).get();
        this.verifyingFindByCodeIsCallingJustOnce(code);
        assertThat(found.getCode()).isEqualTo(code);
    }

    @Test
    public void given3Medications_whenGetAll_thenReturn3Records() {
        Medication medication1 = new Medication();
        Medication medication2 = new Medication();
        Medication medication3 = new Medication();

        medication1.setName("Dipirona");
        medication1.setCode("DIP_76");
        medication1.setImageUrl("localhost:9090/img/3");
        medication1.setWeight(45);

        medication2.setName("Ibuprofeno");
        medication2.setCode("IBP_86");
        medication2.setImageUrl("localhost:9090/img/5");
        medication2.setWeight(86);

        medication3.setName("Karbamazepina");
        medication3.setCode("KAR_12");
        medication3.setImageUrl("localhost:9090/img/45");
        medication3.setWeight(12);

        List<Medication> allMedications = service.fetchAllMedications();
        verifyingFindAllIsCallingOnce();
        assertThat(allMedications).hasSize(3).extracting(Medication::getCode).contains(medication1.getCode(), medication2.getCode(), medication3.getCode());
    }

    private void verifyingFindByCodeIsCallingJustOnce(String code) {
        Mockito.verify(repository, VerificationModeFactory.times(1)).getMedicationByCode(code);
        Mockito.reset(repository);
    }

    private void verifyingFindAllIsCallingOnce() {
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(repository);
    }
}
