package com.toc.drone.constants;

public class WebResourceKeyConstants {

    public final static String URL_DRONE="/api/v1/drone";
    public final static String URL_MEDICATION="/api/v1/medication";

    public interface Endpoints{
        String GET_ALL = "/all";
        String GET_BY_ID = "/id/{id}";
        String REGISTER = "/register";
        String DELETE_ID = "/delete/{id}";
        String UPDATE = "/update";
    }

    public interface DroneEndpoints{
        String CHANGE_STATUS_TO_LOADING="/change-to/loading/{id}";
        String CHANGE_STATUS_TO_IDLE="/change-to/idle/{id}";
        String CHANGE_STATUS_TO_LOADED="/change-to/loaded/{id}";
        String CHANGE_STATUS_TO_DELIVERING="/change-to/delivering/{id}";
        String CHANGE_STATUS_TO_DELIVERED="/change-to/delivered/{id}";
        String CHANGE_STATUS_TO_RETURNING="/change-to/returning/{id}";
    }

    public interface MedicationEndpoints{
        String GET_ALL_MEDICATIONS_BY_A_DRONE="/drone";
        String GET_ALL_MEDICATIONS_BY_A_DRONE_ID="/drone/id/{id}";
        String GET_ALL_MEDICATIONS_BY_A_DRONE_SERIAL="/drone/serial/{serial}";
        String GET_MEDICATION_BY_CODE="/code/{code}";
        String DELETE_MEDICATION_BY_CODE="/delete/code/{code}";
    }

    public interface EspecialEndpoints{
        String LOAD_A_DRONE_MEDICATIONS = "/load-medications";
        String CHECKING_AVAILABLE_DRONES ="/available-drones";
        String CHECK_BATTERY_DRONE = "/check-battery/id/{id}";
    }
}
