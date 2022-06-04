package com.toc.drone.controllers;

import com.toc.drone.constants.WebResourceKeyConstants;
import com.toc.drone.controllers.request.DroneAndMedications;
import com.toc.drone.controllers.request.DroneRequest;
import com.toc.drone.controllers.responses.DroneResponse;
import com.toc.drone.exceptions.*;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;
import com.toc.drone.services.DroneService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Drone Services", value = "This is the drone rest services")
@RequestMapping(WebResourceKeyConstants.URL_DRONE)
public class DroneController {

    private DroneService service;
    private Mapper mapper;

    @Operation(summary = "Get all Drones on the system", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            ))
    })
    @GetMapping(value = WebResourceKeyConstants.Endpoints.GET_ALL, produces = {"application/json"})
    public ResponseEntity<List<DroneResponse>> getAllDrones() {
        List<DroneResponse> listDrones = service.fetchAll()
                .stream()
                .map(p -> {
                    return mapper.map(p, DroneResponse.class);
                }).collect(Collectors.toList());
        return new ResponseEntity<>(listDrones, HttpStatus.OK);
    }

    @Operation(summary = "Get a drone by id", description = "Return a drone", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = DroneResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id Supplied"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @GetMapping(value = WebResourceKeyConstants.Endpoints.GET_BY_ID, produces = {"application/json"})
    public ResponseEntity<DroneResponse> getById(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        return new ResponseEntity(service.getOneDrone(id).map(p -> mapper.map(p, DroneResponse.class)).get(), HttpStatus.OK);
    }

    @Operation(summary = "Register a drone", description = "Return the register drone", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            ))
    })
    @PostMapping(value = WebResourceKeyConstants.Endpoints.REGISTER, produces = {"application/json"})
    public ResponseEntity<DroneResponse> registerDrone(@io.swagger.v3.oas.annotations.parameters.RequestBody
                                                               (description = "A drone to register", required = true, content =
                                                               @Content(schema = @Schema(implementation = DroneResponse.class)))
                                                       @RequestBody DroneRequest request)
            throws BatteryCapacityWrongException, WeightLimitReachedException, ObjectAlreadyExist,DroneCapacityException {
        Drone drone = mapper.map(request, Drone.class);
        DroneResponse response = mapper.map(service.registerDrone(drone), DroneResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Check available drones", description = "Return available drones", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            ))
    })
    @GetMapping(WebResourceKeyConstants.EspecialEndpoints.CHECKING_AVAILABLE_DRONES)
    public ResponseEntity<List<DroneResponse>> getAllAvailableDrones() {
        return new ResponseEntity<List<DroneResponse>>(service.checkAvailableDronesForLoading().stream().map(p -> mapper.map(p, DroneResponse.class)).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Operation(summary = "Check battery level", description = "Return the battery level", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = Integer.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @GetMapping(WebResourceKeyConstants.EspecialEndpoints.CHECK_BATTERY_DRONE)
    public int checkAvailableBatteryDrone(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        return service.checkBatteryLevelForADrone(id);
    }


    @Operation(summary = "Change state to Loading", description = "This service change the state of a drone to loading", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_LOADING)
    public ResponseEntity<?> changeStatusToLoading(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws BatteryIsToLow, NotFoundItemException {
        service.changeStatusToLoading(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Change state to Idle", description = "This service change the state of a drone to idle", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_IDLE)
    public ResponseEntity<?> changeToStatusIdle(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        service.changeStatusToIdle(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Change state to Loaded", description = "This service change the state of a drone to loaded", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_LOADED)
    public ResponseEntity<?> changeToStatusLoaded(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        service.changeStatusToLoaded(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Change state to Delivering", description = "This service change the state of a drone to delivering", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_DELIVERING)
    public ResponseEntity<?> changeToStatusDelivering(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        service.changeStatusToDelivering(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Change state to Delivered", description = "This service change the state of a drone to delivered", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_DELIVERED)
    public ResponseEntity<?> changeToStatusDelivered(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        service.changeStatusToDelivered(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Change state to Returning", description = "This service change the state of a drone to returning", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.DroneEndpoints.CHANGE_STATUS_TO_RETURNING)
    public ResponseEntity<?> changeToStatusReturning(@Parameter(description = "Id of the drone", required = true) @PathVariable("id") long id) throws NotFoundItemException {
        service.changeStatusToReturning(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Update a drone", description = "Return the drone updated", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not drone found")
    })
    @PutMapping(WebResourceKeyConstants.Endpoints.UPDATE)
    public ResponseEntity<?> updateDrone(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The drone to update", required = true, content = @Content(
            schema = @Schema(implementation = DroneResponse.class)
    ))
                                         @RequestBody DroneResponse request) {
        Optional<Drone> drone = service.updateDrone(mapper.map(request, Drone.class));
        return ResponseEntity.ok(mapper.map(drone, DroneResponse.class));
    }

    @Operation(summary = "Load a drone with medications", description = "Return the drone loaded", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "An exception has occurred")
    })
    @PostMapping(WebResourceKeyConstants.EspecialEndpoints.LOAD_A_DRONE_MEDICATIONS)
    public ResponseEntity<DroneResponse> loadDroneWithMedications(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The drone to load and the medication's list", required = true, content = @Content(
            schema = @Schema(implementation = DroneAndMedications.class)
    ))@RequestBody DroneAndMedications droneAndMedications) throws WeightLimitReachedException,NotFoundItemException,BatteryIsToLow,InvalidCodeFormatException{
        Drone drone = mapper.map(droneAndMedications.getDrone(),Drone.class);

        List<Medication> medicationsList = droneAndMedications.getMedications().stream().map(medication->{
            return mapper.map(medication,Medication.class);
        }).collect(Collectors.toList());

        Optional<Drone> droneOptional = service.loadDroneWithMedications(drone,medicationsList);

        if(droneOptional.isPresent()){
            return ResponseEntity.ok(droneOptional.map(drone1 -> mapper.map(drone1,DroneResponse.class)).get());
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Load a drone with medications", description = "Return the drone loaded", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Drone not found")
    })
    @DeleteMapping(WebResourceKeyConstants.Endpoints.DELETE_ID)
    public ResponseEntity<?> deleteDroneById(@Parameter(description = "Id of the drone", required = true)@PathVariable("id")long id) throws NotFoundItemException{
        service.deleteDroneById(id);
        return ResponseEntity.ok(null);
    }


    @Autowired
    public DroneController(DroneService service, Mapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }


}
