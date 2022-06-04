package com.toc.drone.services;

import com.toc.drone.models.Drone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    private DroneService service;

    private static final Logger log= LoggerFactory.getLogger(ScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 60000)
    public void reportBatteryLevel(){
        List<Drone> droneList= service.fetchAll();
        if(droneList.isEmpty()){
            log.info("Not drones yet");
        }
        else {
            for (Drone drone : droneList) {
                log.info("The drone " + drone.getSerial()+ " has a " + drone.getBatteryCapacity() + "% of battery at "+ dateFormat.format(new Date()));
            }
        }
    }

    @Autowired
    public ScheduledTask(DroneService service){
        this.service=service;
    }
}
