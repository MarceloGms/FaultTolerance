package com.qcs;

import java.util.ArrayList;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("\n-------\n FR2: Cruise Control\n");

        int setSpeed = 120;
        int error = 0;
        int maxAccel = 10;
        int currentSpeed = 100;

        ArrayList<Integer> prevSpeed = new ArrayList<>();
        prevSpeed.add(100);
        prevSpeed.add(100);

        ArrayList<Integer> speedReadings = new ArrayList<>();
        speedReadings.add(100);
        speedReadings.add(100);
        speedReadings.add(10000); // outlier

        // No fault Tolerance
        double accel = CruiseControl.calcAccel(setSpeed, error, maxAccel, currentSpeed, prevSpeed);
        System.out.printf(" Output: %.2f\n", accel);

        // With Fault Tolerance
        int crcOriginal = CruiseControlFT.computeCRC(speedReadings);
        double faultToleranceAccel = CruiseControlFT.CalcAccel(setSpeed, error, maxAccel, speedReadings, prevSpeed, crcOriginal);
        System.out.printf(" FT Output: %.2f\n", faultToleranceAccel);

        System.out.println("-------");
    }
}
