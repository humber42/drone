package com.toc.drone.testing.repository;

import com.toc.drone.models.Medication;
import com.toc.drone.repository.MedicationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MedicationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicationRepository repository;

    @Test
    public void whenFindAMedicationByCode_thenReturnAMedication() {
        Medication medication = new Medication();
        medication.setName("Dipirona");
        medication.setCode("DIP_76");
        medication.setImageUrl("localhost:9090/img/3");
        medication.setWeight(45);

        entityManager.persistAndFlush(medication);

        Medication found = repository.getMedicationByCode(medication.getCode()).orElse(null);
        assertThat(found.getCode()).isEqualTo(medication.getCode());
    }

    @Test
    public void whenProvideAMedication_thenRemove() {
        Medication medication = new Medication();
        medication.setName("Dipirona");
        medication.setCode("DIP_76");
        medication.setImageUrl("localhost:9090/img/3");
        medication.setWeight(45);

        entityManager.persistAndFlush(medication);
        Medication found = repository.getMedicationByCode(medication.getCode()).orElse(null);
        repository.deleteByCode(found.getCode());
        Medication foundAgain = repository.getMedicationByCode(medication.getCode()).orElse(null);
        assertThat(foundAgain).isNull();
    }
}
