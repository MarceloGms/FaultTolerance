package com.qcs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CruiseControlFT {
   private static final int SAFE_ACCEL = 2;
   public static double faultToleranceCalcAccel(int setSpeed, int error, int maxAccel, List<Integer> speedReadings, List<Integer> prevSpeeds, int crcOriginal) {
      // Validate parameters
      if (maxAccel <= 0) {
         System.out.println("Invalid max acceleration. Using safe fallback value.");
         maxAccel = SAFE_ACCEL;
      }

      int currentSpeed = 0;
      if (speedReadings != null && !speedReadings.isEmpty()) {
            speedReadings = speedReadings.stream()
         .filter(speed -> speed > 0 && speed < 300) // Valid range
         .collect(Collectors.toList());
         // Calculate reliable speed
         currentSpeed = getMedian(speedReadings);
      } else {
         if (prevSpeeds == null || prevSpeeds.isEmpty()) {
            System.out.println("No previous speeds available. Cannot compute acceleration.");
            return 0.0;
         }
         System.out.println("Current speed readings are empty. Using previous speeds only.");
         // Continuing with current speed as 0
      }

      // CRC
      int crcRecomputed = computeCRC(speedReadings);
      // todo: should i do this for prev speeds too?
      if (crcOriginal != crcRecomputed) {
         throw new IllegalArgumentException("Data corruption detected! Invalid speed readings.");
      }

      if (setSpeed <= 0) {
         System.out.println("Invalid set speed. Not applying acceleration.");
         return 0.0;
      }

      if (error < 0) {
         System.out.println("Invalid error value. Using error = 0.");
         error = 0;
      }

      if (prevSpeeds != null && !prevSpeeds.isEmpty()) {
         prevSpeeds = prevSpeeds.stream()
         .filter(speed -> speed > 0 && speed < 300) // Valid speed range 0-300 km/h
         .collect(Collectors.toList());
         
         final int SPEED_HISTORY_LIMIT = 5;
         if (prevSpeeds.size() >= SPEED_HISTORY_LIMIT) {
            prevSpeeds = prevSpeeds.subList(prevSpeeds.size() - SPEED_HISTORY_LIMIT, prevSpeeds.size());
         }
         if (currentSpeed > 0) {
            prevSpeeds.add(currentSpeed);
         }
      } else {
         if (currentSpeed > 0) {
            System.out.println("Previous speeds are empty. Using current speed only.");
            prevSpeeds = Arrays.asList(currentSpeed);
         } else {
            System.out.println("No valid speed readings available. Cannot compute acceleration.");
            return 0.0;
         }
      }
            
      // Median of previous speeds instead of average
      int reliableSpeed = getMedian(prevSpeeds);
      int lowerBound = setSpeed - error;
      int upperBound = setSpeed + error;

      if (reliableSpeed >= lowerBound && reliableSpeed <= upperBound) {
         return 0.0; // No acceleration needed
      }

      // Triple Modular Redundancy
      double result1 = computeAcceleration(setSpeed, reliableSpeed, maxAccel);
      double result2 = computeAcceleration(setSpeed, reliableSpeed, maxAccel);
      double result3 = computeAcceleration(setSpeed, reliableSpeed, maxAccel);
      double finalAccel = majorityVote(result1, result2, result3);

      return finalAccel;
   }

   private static double computeAcceleration(int setSpeed, int currentSpeed, int maxAccel) {
      double requiredChange = setSpeed - currentSpeed;
      double acceleration = Math.min(Math.abs(requiredChange), maxAccel);
      return requiredChange > 0 ? acceleration : -acceleration;
   }

   private static int getMedian(List<Integer> values) {
      List<Integer> sorted = values.stream().sorted().collect(Collectors.toList());
      int size = sorted.size();
      
      if (size % 2 == 1) {
         return sorted.get(size / 2); // Odd size: return the middle element
      } else {
         return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2; // Even size: average of two middle elements
      }
   }

   private static double majorityVote(double a, double b, double c) {
      if (a == b || a == c) return a;
      if (b == c) return b;
      
      // No direct majority -> return the closest to the median value
      double median = getMedian(Arrays.asList((int) a, (int) b, (int) c));
      return (Math.abs(a - median) <= Math.abs(b - median) && Math.abs(a - median) <= Math.abs(c - median)) ? a
           : (Math.abs(b - median) <= Math.abs(a - median) && Math.abs(b - median) <= Math.abs(c - median)) ? b : c;
   }

   public static int computeCRC(List<Integer> data) {
      int crc = 0xFFFF; // Initial CRC value

      for (int value : data) {
         crc ^= value; // XOR with current data

         for (int i = 0; i < 8; i++) { // 8-bit checksum
            if ((crc & 1) != 0) {
                  crc = (crc >> 1) ^ 0xA001; // Polynomial 0xA001 (CRC-16)
            } else {
                  crc >>= 1;
            }
         }
      }
      return crc;
   }
}
