package com.toc.drone.exceptions;

public class BatteryIsToLow extends Exception{
    public BatteryIsToLow(String msg){
        super(msg);
    }
}
