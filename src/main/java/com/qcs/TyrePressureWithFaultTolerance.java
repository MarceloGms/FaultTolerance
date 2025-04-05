package com.qcs;

import java.util.ArrayList;
import java.util.List;

public class TyrePressureWithFaultTolerance {

    public static List<Integer> getTyresWithLowPressure(List<List<Integer>> pressures, int targetPressure) {
        if (pressures == null || pressures.isEmpty()) {
            throw new IllegalArgumentException("Error: Pressure data cannot be null or empty.");
        }

        List<Integer> result = new ArrayList<>();
        List<Double> processedPressures = new ArrayList<>();
        List<Integer> faultySensors = new ArrayList<>();

        for (int i = 0; i < pressures.size(); i++) {
            List<Integer> tyrePressures = pressures.get(i);
            if (tyrePressures == null || tyrePressures.isEmpty()) {
                throw new IllegalArgumentException("Error: Tyre " + i + " has no pressure readings."); // 🔹 Tratamento de Falhas
            }

            // Verificação de valores negativos
            for (int pressure : tyrePressures) {
                if (pressure < 0) {
                    throw new IllegalArgumentException("Error: Negative pressure detected in tyre " + i);
                }
            }

            double filteredPressure = calculateFilteredPressure(tyrePressures);
            if (filteredPressure == Double.MAX_VALUE) {
                faultySensors.add(i); // 🔹 Isolation
            }
            processedPressures.add(filteredPressure);
        }

        // Identificar pneus com baixa pressão
        for (int i = 0; i < processedPressures.size(); i++) {
            if (processedPressures.get(i) <= targetPressure) {
                result.add(i);
            }
        }

        // Diagnóstico de sensores falhados
        if (!faultySensors.isEmpty()) {
            System.out.println("Warning: Faulty sensors detected in tyres: " + faultySensors);
        }

        // Verificação de falha crítica
        if (result.isEmpty() && faultySensors.size() == pressures.size()) {
            throw new IllegalStateException("Critical Error: All sensors have failed. Unable to determine tyre pressures.");
        }

        return result;
    }

    private static double calculateFilteredPressure(List<Integer> pressures) {
        List<Integer> validPressures = new ArrayList<>();
        double sum = 0;
        int count = 0;
        int zeroCount = 0;

        for (int pressure : pressures) {
            if (pressure == 0) { // 🔹 Concurrent Detection 
                zeroCount++;
                continue;
            }
            validPressures.add(pressure);
            sum += pressure;
            count++;
        }

        if (zeroCount == pressures.size()) {
            return Double.MAX_VALUE; // 🔹 Rollforward
        }

        double mean = (count > 0) ? sum / count : Double.MAX_VALUE;
        double median = calculateMedian(validPressures);

        // Combinar média e mediana, atribuindo mais peso à mediana para maior estabilidade
        return (mean * 0.4) + (median * 0.6); // 🔹 Compensation
    }

    private static double calculateMedian(List<Integer> values) {
        if (values.isEmpty()) return Double.MAX_VALUE;
        List<Integer> sorted = new ArrayList<>(values);
        sorted.sort(Integer::compareTo);
        int mid = sorted.size() / 2;
        return (sorted.size() % 2 == 0) ? 
            (sorted.get(mid - 1) + sorted.get(mid)) / 2.0 : 
            sorted.get(mid);
    }
}