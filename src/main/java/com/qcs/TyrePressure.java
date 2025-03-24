/* FR3 - Tyre pressure warning
The system must control tyre pressure and produce a warning when the pressure in a tyre (in PSI) is lower
than a predefined value. To avoid intermittent warnings (i.e., warnings that appear and disappear quickly)
when pressures are close to the predefined value (and can vary due to heat and other factors), the system
should receive a list of previous pressure readings, calculate the average pressure and consider that value.
Sometimes the tyre pressure may malfunction, when this failure of the sensor is detected via hardware, the
reading is set to 0 PSI. If a reading of 0 PSI is received, the system should ignore the reading.
This functionality must be implemented by a function that receives the required parameters (namely, the
current and past tyre pressure readings and the target pressure level below which a warning is thrown) and
returns a list with the tyres that, ordered from the tyre with the lowest pressure to the tyre with the highest
pressure.
For reference, your code must pass the following test cases:

Input:
pressures: [ [10], [10], [10], [10]]
target_pressure: 5 
Expected Output: []

input:
pressures: [ [10, 10], [30, 30], [20, 20], [40, 40]]
target_pressure: 100 
Expected Output: [0, 2, 1, 3] */

package com.qcs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TyrePressure {

    public static List<Integer> getTyresWithLowPressure(List<List<Integer>> pressures, int targetPressure) {
        List<Integer> result = new ArrayList<>();
        List<Double> averagePressures = new ArrayList<>();

        // Calculate the average pressure for each tyre, ignoring 0 PSI readings
        for (List<Integer> tyrePressures : pressures) {
            double sum = 0;
            int count = 0;
            for (int pressure : tyrePressures) {
                if (pressure != 0) { // Ignore 0 PSI readings
                    sum += pressure;
                    count++;
                }
            }
            // Add the average pressure or 0 if no valid readings
            averagePressures.add(count > 0 ? sum / count : 0);
        }

        // Collect indices of tyres with pressure below the target
        for (int i = 0; i < averagePressures.size(); i++) {
            if (averagePressures.get(i) < targetPressure) {
                result.add(i);
            }
        }

        // Sort the indices based on the average pressures in ascending order
        result.sort(Comparator.comparingDouble(averagePressures::get));

        return result;
    }
}