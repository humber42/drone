package com.toc.drone.services;

import com.toc.drone.exceptions.InvalidCodeFormatException;
import com.toc.drone.exceptions.NotFoundItemException;
import com.toc.drone.exceptions.ObjectAlreadyExist;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;
import com.toc.drone.repository.MedicationRepository;
import com.toc.drone.utils.VerificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {


    MedicationRepository medicationRepository;

    public List<Medication> getAllMedicationsCarriedByADrone(Drone drone) throws NotFoundItemException{
        return medicationRepository.getAllByDrone(drone);
    }

    public List<Medication> getAllMedicationCarriedByIdDrone(long id) throws NotFoundItemException{
        return medicationRepository.getAllByDrone_Id(id);
    }

    public List<Medication> getAllMedicationCarriedByASerialDrone(String serial) throws NotFoundItemException{
        return medicationRepository.getAllByDrone_Serial(serial);
    }

    public List<Medication> fetchAllMedications(){
        return medicationRepository.findAll();
    }

    public Optional<Medication> getMedicationByCode(String code) throws InvalidCodeFormatException,NotFoundItemException {
        VerificationUtils.codeFormatVerification(code);
        Optional<Medication> medication= medicationRepository.getMedicationByCode(code);
        if(!medication.isPresent()){
            throw new NotFoundItemException("Medication not found");
        }
        else return medication;
    }

    public Optional<Medication> getMedicationById(long id) throws NotFoundItemException{
        Optional<Medication> medication = medicationRepository.findById(id);
        if(!medication.isPresent()){
            throw new NotFoundItemException("Medication not found");
        }
        else return medication;
    }

    public Medication registerMedication(Medication medication) throws InvalidCodeFormatException,ObjectAlreadyExist{
        VerificationUtils.codeFormatVerification(medication.getCode());
        try {
            if (this.getMedicationByCode(medication.getCode()).isPresent()) {
                throw new ObjectAlreadyExist("Exist one medication with that code");
            }
        }catch (NotFoundItemException e){
            return medicationRepository.save(medication);
        }
        return null;
    }

    public void deleteMedicationById(long id) throws NotFoundItemException{
        this.getMedicationById(id);
        medicationRepository.deleteById(id);
    }

    public void deleteMedicationByCode(String code) throws InvalidCodeFormatException,NotFoundItemException{
        VerificationUtils.codeFormatVerification(code);
        this.getMedicationByCode(code);
        medicationRepository.deleteByCode(code);
    }

    public Medication updateMedication(Medication medication) throws InvalidCodeFormatException{
        VerificationUtils.codeFormatVerification(medication.getCode());
        return medicationRepository.saveAndFlush(medication);
    }

    @Autowired
    public MedicationService(MedicationRepository medicationRepository){
        this.medicationRepository= medicationRepository;
    }
}
