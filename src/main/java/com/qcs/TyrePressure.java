package com.qcs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TyrePressure {

    public static List<Integer> getTyresWithLowPressure(List<List<Integer>> pressures, int targetPressure) {
        List<Integer> result = new ArrayList<>();
        List<Double> averagePressures = new ArrayList<>();

        for (List<Integer> tyrePressures : pressures) {
            double sum = 0;
            int count = 0;
            for (int pressure : tyrePressures) {
                    sum += pressure;
                    count++;
            }
            averagePressures.add(count > 0 ? sum / count : 0);
        }

        for (int i = 0; i < averagePressures.size(); i++) {
            if (averagePressures.get(i) < targetPressure) {
                result.add(i);
            }
        }

        result.sort(Comparator.comparingDouble(averagePressures::get));

        return result;
    }
}