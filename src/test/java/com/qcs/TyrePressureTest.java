package com.qcs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TyrePressureTest {

    @Test
    void testNoLowPressureTyres() {
        List<List<Integer>> pressures = List.of(
            List.of(10), List.of(10), List.of(10), List.of(10)
        );
        int targetPressure = 5;
        assertEquals(List.of(), TyrePressureWithFaultTolarance.getTyresWithLowPressure(pressures, targetPressure));
    }

    @Test
    void testAllTyresAboveTarget() {
        List<List<Integer>> pressures = List.of(
            List.of(10, 10), List.of(10, 10), List.of(10, 10), List.of(10, 10)
        );
        int targetPressure = 5;
        assertEquals(List.of(), TyrePressureWithFaultTolarance.getTyresWithLowPressure(pressures, targetPressure));
    }

    @Test
    void testAllTyresBelowTarget() {
        List<List<Integer>> pressures = List.of(
            List.of(10, 10), List.of(30, 30), List.of(20, 20), List.of(40, 40)
        );
        int targetPressure = 100;
        assertEquals(List.of(0, 2, 1, 3), TyrePressureWithFaultTolarance.getTyresWithLowPressure(pressures, targetPressure));
    }

    @Test
    void testSingleLowPressureTyre() {
        List<List<Integer>> pressures = List.of(
            List.of(10), List.of(1), List.of(10), List.of(10)
        );
        int targetPressure = 5;
        assertEquals(List.of(1), TyrePressureWithFaultTolarance.getTyresWithLowPressure(pressures, targetPressure));
    }

    @Test
    void testIgnoreZeroPressureReadings() {
        List<List<Integer>> pressures = List.of(
            List.of(0, 15), List.of(10, 10), List.of(10, 10), List.of(10, 10)
        );
        int targetPressure = 8;
        assertEquals(List.of(), TyrePressureWithFaultTolarance.getTyresWithLowPressure(pressures, targetPressure));
    }
}
