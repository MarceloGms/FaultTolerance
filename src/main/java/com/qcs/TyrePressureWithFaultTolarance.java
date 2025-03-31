package com.qcs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TyrePressureWithFaultTolarance {

    public static List<Integer> getTyresWithLowPressure(List<List<Integer>> pressures, int targetPressure) {
        List<Integer> result = new ArrayList<>();
        List<Double> averagePressures = new ArrayList<>();
        List<Integer> faultySensors = new ArrayList<>(); // Lista para diagnóstico de sensores falhados

        // Calcular a média da pressão para cada pneu, ignorando leituras inválidas
        for (int i = 0; i < pressures.size(); i++) {
            List<Integer> tyrePressures = pressures.get(i);
            double sum = 0;
            int count = 0;
            int zeroCount = 0;

            for (int pressure : tyrePressures) {
                if (pressure == 0) { 
                    zeroCount++;
                    continue; // 🔹 ISOLAMENTO: Ignorar leituras de 0 PSI (falha do sensor)
                }
                sum += pressure;
                count++;
            }

            // Se todas as leituras forem 0, marcar o sensor como defeituoso
            if (zeroCount == tyrePressures.size()) {
                faultySensors.add(i); // 🔹 DIAGNÓSTICO: Registar sensores falhados para análise
                averagePressures.add(Double.MAX_VALUE); // 🔹 COMPENSAÇÃO: Usar um valor alto para mover este pneu para o fim da lista
            } else {
                // Calcular média, se houver leituras válidas
                averagePressures.add(count > 0 ? sum / count : Double.MAX_VALUE);
            }
        }

        // Identificar pneus com pressão abaixo do limite
        for (int i = 0; i < averagePressures.size(); i++) {
            if (averagePressures.get(i) < targetPressure) {
                result.add(i);
            }
        }

        // Ordenar os pneus com baixa pressão do menor para o maior
        result.sort(Comparator.comparingDouble(averagePressures::get));

        // Log de sensores falhados para diagnóstico
        if (!faultySensors.isEmpty()) {
            System.out.println("Warning: Faulty sensors detected in tyres: " + faultySensors); 
            // 🔹 DIAGNÓSTICO: Emitir alerta sobre sensores falhados
        }

        return result;
    }
}
