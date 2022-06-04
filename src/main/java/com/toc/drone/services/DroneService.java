package com.toc.drone.services;

import com.toc.drone.exceptions.*;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;
import com.toc.drone.models.State;
import com.toc.drone.repository.DroneRepository;
import com.toc.drone.utils.VerificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {


    private DroneRepository repository;
    private MedicationService medicationService;

    public Optional<Drone> getOneDrone(long id) throws NotFoundItemException {
        Optional<Drone> drone = repository.findById(id);
        if (!drone.isPresent()) {
            throw new NotFoundItemException("Not item found");
        }
        return drone;
    }

    public void changeStatusToLoading(long id) throws BatteryIsToLow,NotFoundItemException {
        if(this.checkBatteryLevelForADrone(id)>25){
            this.getOneDrone(id).ifPresent(drone -> {
                drone.setState(State.LOADING);
                this.updateDrone(drone);
            });
        }else {
            throw new BatteryIsToLow("The battery level must be eager than 25%");
        }
    }

    public void changeStatusToIdle(long id) throws NotFoundItemException{
        this.getOneDrone(id).ifPresent(drone -> {
            drone.setState(State.IDLE);
            this.updateDrone(drone);
        });
    }

    public void changeStatusToLoaded(long id) throws NotFoundItemException{
        this.getOneDrone(id).ifPresent(drone->{
            drone.setState(State.LOADED);
            this.updateDrone(drone);
        });
    }

    public void changeStatusToDelivering(long id) throws NotFoundItemException{
        this.getOneDrone(id).ifPresent(drone -> {
            drone.setState(State.DELIVERING);
            this.updateDrone(drone);
        });
    }

    public void changeStatusToDelivered(long id)throws NotFoundItemException{
        this.getOneDrone(id).ifPresent(drone->{
            drone.setState(State.DELIVERED);
            drone.getMedications().forEach(medication -> {
                medication.setDrone(null);
                try {
                    medicationService.updateMedication(medication);
                } catch (InvalidCodeFormatException e) {
                    e.printStackTrace();
                }
            });
            drone.setMedications(new LinkedList<>());
            this.updateDrone(drone);
        });
    }

    public void changeStatusToReturning(long id) throws NotFoundItemException{
        this.getOneDrone(id).ifPresent(drone->{
            drone.setState(State.RETURNING);
            this.updateDrone(drone);
        });
    }

    public Optional<Drone> updateDrone(Drone drone){
        return Optional.of(repository.saveAndFlush(drone));
    }

    public Optional<Drone> getDroneBySerial(String serial) {
        return repository.getDroneBySerial(serial);
    }

    public List<Drone> fetchAll() {
        return repository.findAll();
    }

    public Drone registerDrone(Drone drone) throws BatteryCapacityWrongException, WeightLimitReachedException, ObjectAlreadyExist, DroneCapacityException {

        if(repository.count()==10){
            throw new DroneCapacityException("The drone capacity is full");
        }
        else if (drone.getBatteryCapacity() > 100) {
            throw new BatteryCapacityWrongException("Battery Capacity Exceeded");
        } else if (drone.getWeightLimit() > 500) {
            throw new WeightLimitReachedException("The limit of the capacity must be less than 500");
        } else if (this.getDroneBySerial(drone.getSerial()).isPresent()) {
            throw new ObjectAlreadyExist("The drone already exist");
        } else {
            drone.setState(State.IDLE);
            return repository.save(drone);
        }
    }

    public List<Drone> checkAvailableDronesForLoading() {
        return repository.findAll()
                .stream()
                .filter(p -> p.getState().equals(State.IDLE))
                .collect(Collectors.toList());
    }

    public void deleteDroneById(long id) throws NotFoundItemException{
        this.getOneDrone(id);
        repository.deleteById(id);
    }

    public int checkBatteryLevelForADrone(Drone drone) throws NotFoundItemException {
        Optional<Drone> drone1 = this.getOneDrone(drone.getId());
        return drone1.get().getBatteryCapacity();
    }

    public int checkBatteryLevelForADrone(long id) throws NotFoundItemException {
        Optional<Drone> drone1 = this.getOneDrone(id);
        return drone1.get().getBatteryCapacity();
    }

    public Optional<Drone> loadDroneWithMedications(Drone drone, List<Medication> medications) throws WeightLimitReachedException, NotFoundItemException,BatteryIsToLow,InvalidCodeFormatException{
        this.changeStatusToLoading(drone.getId());
        for (Medication medication : medications) {
            if (VerificationUtils.weightLimitReachedVerification(drone, medication)) {
                medication.setDrone(drone);
                medicationService.updateMedication(medication);
            }
        }
        this.changeStatusToLoading(drone.getId());
        return this.getOneDrone(drone.getId());
    }

    @Autowired
    public DroneService(DroneRepository repository, MedicationService medicationService) {
        this.repository = repository;
        this.medicationService = medicationService;
    }

    public DroneService() {

    }

}
