package com.toc.drone.utils;

import com.toc.drone.exceptions.InvalidCodeFormatException;
import com.toc.drone.exceptions.WeightLimitReachedException;
import com.toc.drone.models.Drone;
import com.toc.drone.models.Medication;

public class VerificationUtils {

    public static boolean codeFormatVerification(String code) throws InvalidCodeFormatException {
        for(char character: code.toCharArray()){
            if (!(Character.isDigit(character)||Character.isUpperCase(character)||character=='_')){
                throw new InvalidCodeFormatException("Invalid Code Format");
            }
        }
        return true;
    }

    public static boolean weightLimitReachedVerification(Drone drone, Medication medication) throws WeightLimitReachedException {
        int weightCharged = 0;
        for(Medication medication1:drone.getMedications()){
            weightCharged+=medication1.getWeight();
        }
        if(drone.getWeightLimit()<weightCharged+medication.getWeight()){
            throw new WeightLimitReachedException("Limit of weight has been exceeded");
        }
        return true;
    }

}
