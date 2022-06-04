package com.toc.drone.controllers;

import com.toc.drone.constants.WebResourceKeyConstants;
import com.toc.drone.controllers.request.DroneAndMedications;
import com.toc.drone.controllers.request.MedicationRequest;
import com.toc.drone.controllers.responses.DroneResponse;
import com.toc.drone.controllers.responses.MedicationResponse;
import com.toc.drone.exceptions.InvalidCodeFormatException;
import com.toc.drone.exceptions.NotFoundItemException;
import com.toc.drone.exceptions.ObjectAlreadyExist;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;
import com.toc.drone.services.MedicationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Medication Service", value = "This is the medication rest services")
@RequestMapping(WebResourceKeyConstants.URL_MEDICATION)
public class MedicationController {

    private MedicationService service;
    private Mapper mapper;

    @Operation(summary = "Get all medications carried by a drone", description = "Return the medication list", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not found drone")
    })
    @PostMapping(WebResourceKeyConstants.MedicationEndpoints.GET_ALL_MEDICATIONS_BY_A_DRONE)
    public ResponseEntity<List<MedicationResponse>> getAllMedicationCarriedByADrone(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The drone", required = true, content = @Content(
                    schema = @Schema(implementation = DroneResponse.class)
            ))@RequestBody DroneResponse drone) throws NotFoundItemException {
        return ResponseEntity.ok(service.getAllMedicationsCarriedByADrone(mapper.map(drone, Drone.class))
                .stream()
                .map(this::mapping)
                .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Get all medications carried by a drone", description = "Return the medication list", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not found drone")
    })
    @GetMapping(WebResourceKeyConstants.MedicationEndpoints.GET_ALL_MEDICATIONS_BY_A_DRONE_ID)
    public ResponseEntity<List<MedicationResponse>> getAllMedicationCarriedByADroneId(@Parameter(description = "Id of the drone", required = true)@PathVariable("id")long id) throws NotFoundItemException{
        return ResponseEntity.ok(
                service.getAllMedicationCarriedByIdDrone(id)
                        .stream()
                        .map(this::mapping)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Get all medications carried by a drone", description = "Return the medication list", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not found drone")
    })
    @GetMapping(WebResourceKeyConstants.MedicationEndpoints.GET_ALL_MEDICATIONS_BY_A_DRONE_SERIAL)
    public ResponseEntity<List<MedicationResponse>> getAllMedicationCarriedByADroneSerial(@Parameter(description = "Serial of the drone", required = true)@PathVariable("serial") String serial) throws NotFoundItemException{
        return ResponseEntity.ok(
                service.getAllMedicationCarriedByASerialDrone(serial)
                        .stream()
                        .map(this::mapping)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Get all medications", description = "Return the medication list", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            ))
    })
    @GetMapping(WebResourceKeyConstants.Endpoints.GET_ALL)
    public ResponseEntity<List<MedicationResponse>> getAllMedication(){
        return ResponseEntity.ok(
                service.fetchAllMedications()
                .stream()
                .map(this::mapping)
                .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Get medication by code", description = "Return the medication ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not found drone")
    })
    @GetMapping(WebResourceKeyConstants.MedicationEndpoints.GET_MEDICATION_BY_CODE)
    public ResponseEntity<MedicationResponse> getMedicationByCode(@Parameter(description = "Medication code", required = true)@PathVariable("code")String code)throws InvalidCodeFormatException,NotFoundItemException {
        return ResponseEntity.ok(service.getMedicationByCode(code).map(this::mapping).get());
    }

    @Operation(summary = "Get medication by id", description = "Return the medication ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Not found drone")
    })
    @GetMapping(WebResourceKeyConstants.Endpoints.GET_BY_ID)
    public ResponseEntity<MedicationResponse> getMedicationById(@Parameter(description = "Medication id", required = true)@PathVariable("id") long id) throws NotFoundItemException{
        return ResponseEntity.ok(service.getMedicationById(id).map(this::mapping).get());
    }

    @Operation(summary = "Register a medication", description = "Return the registered medication ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "An exception has occurred")
    })
    @PostMapping(WebResourceKeyConstants.Endpoints.REGISTER)
    public ResponseEntity<MedicationResponse> registerMedication(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The medication", required = true, content = @Content(
                    schema = @Schema(implementation = MedicationRequest.class)
            ))
            @RequestBody MedicationRequest request)throws InvalidCodeFormatException, ObjectAlreadyExist {
        Medication response = service.registerMedication(mapper.map(request, Medication.class));
        return ResponseEntity.ok(mapper.map(response, MedicationResponse.class));
    }

    @Operation(summary = "Update a medication", description = "Return the update medication ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Invalid code format")
    })
    @PutMapping(WebResourceKeyConstants.Endpoints.UPDATE)
    public ResponseEntity<MedicationResponse> updateMedication(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The medication", required = true, content = @Content(
                    schema = @Schema(implementation = MedicationResponse.class)
            ))
            @RequestBody MedicationResponse request) throws InvalidCodeFormatException{
        return ResponseEntity.ok(mapping(service.updateMedication(mapper.map(request, Medication.class))));
    }


    @Operation(summary = "Delete medication by id", description = "Delete a medication by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Not medication found")
    })
    @DeleteMapping(WebResourceKeyConstants.Endpoints.DELETE_ID)
    public ResponseEntity<?> deleteMedicationById(@Parameter(description = "Medication id", required = true)@PathVariable("id") long id) throws NotFoundItemException{
        service.deleteMedicationById(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Delete medication by code", description = "Delete a medication by code", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "An exception has occurred")
    })
    @DeleteMapping(WebResourceKeyConstants.MedicationEndpoints.DELETE_MEDICATION_BY_CODE)
    public ResponseEntity<?> deleteMedicationByCode(@Parameter(description = "Medication code", required = true)@PathVariable("code") String code) throws InvalidCodeFormatException,NotFoundItemException{
        service.deleteMedicationByCode(code);
        return ResponseEntity.ok(null);
    }

    private MedicationResponse mapping(Medication medication){
        return mapper.map(medication, MedicationResponse.class);
    }

    @Autowired
    public MedicationController(MedicationService service,Mapper mapper){
        this.service=service;
        this.mapper=mapper;
    }

}
