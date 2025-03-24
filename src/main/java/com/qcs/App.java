package com.qcs;

import java.util.ArrayList;

/* problems:
software bugs
errors passing parameters calling our function like wrong order
sensors can break and give us wrong data like 10000
radiation from the sun can eat the cpu (soft errors)  */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("\n-------\n FR2: Cruise Control\n-------\n");

        // Example parameters for CruiseControl
        int setSpeed = 120; // Desired speed in km/h
        int error = 0;      // Acceptable error in km/h
        int maxAccel = 10;  // Maximum acceleration in km/h²
        int currentSpeed = 100; // Current speed in km/h

        // Initialize prevSpeed with [100, 100]
        ArrayList<Integer> prevSpeed = new ArrayList<>();
        prevSpeed.add(100);
        prevSpeed.add(100);

        // Create a CruiseControl instance
        CruiseControl cruiseControl = new CruiseControl(setSpeed, error, maxAccel, currentSpeed, prevSpeed);

        // Call calculateAcceleration with the current speed
        double acceleration = cruiseControl.calculateAcceleration();

        // Print the output
        System.out.printf(" Output: %.2f\n", acceleration);

        System.out.println("\n-------\n FR3: Tyre Pressure\n-------\n");
        new TyrePressure();
    }
}
