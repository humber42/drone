package com.toc.drone.exceptions;

public class WeightLimitReachedException extends Exception{

    public WeightLimitReachedException(String message){
        super(message);
    }
}
