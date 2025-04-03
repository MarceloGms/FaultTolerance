package com.qcs;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CruiseControl {
   public static double calcAccel(int setSpeed, int error, int maxAccel, int currentSpeed, List<Integer> prevSpeeds) {
      // Calculate average speed
      double sum = 0;
      for (int speed : prevSpeeds) {
         sum += speed;
      }
      double averageSpeed = sum / prevSpeeds.size();

      int lowerBound = setSpeed - error;
      int upperBound = setSpeed + error;

      if (averageSpeed >= lowerBound && averageSpeed <= upperBound) {
         return 0.0; // No acceleration needed
      }

      // Calculate acceleration
      double requiredChange = setSpeed - currentSpeed;
      double acceleration = Math.min(Math.abs(requiredChange), maxAccel);

      return requiredChange > 0 ? acceleration : -acceleration;
   }


   public static double faultToleranceCalcAccel(int setSpeed, int error, int maxAccel, List<Integer> speedReadings, List<Integer> prevSpeeds) {
      // Validate parameters
      if (maxAccel <= 0) {
         throw new IllegalArgumentException("Max acceleration must be non-negative.");
      }
      if (speedReadings == null || speedReadings.isEmpty()) {
         throw new IllegalArgumentException("Speed readings cannot be empty.");
      }
      speedReadings = speedReadings.stream()
        .filter(speed -> speed > 0 && speed < 300) // Valid range
        .collect(Collectors.toList());

      // Calculate reliable speed
      int currentSpeed = getMedian(speedReadings);
      if (setSpeed <= 0 || currentSpeed < 0) {
         throw new IllegalArgumentException("Speed values must be non-negative.");
      }
      if (error < 0) {
         throw new IllegalArgumentException("Error must be non-negative.");
      }
      if (prevSpeeds == null || prevSpeeds.isEmpty()) {
         prevSpeeds = Arrays.asList(currentSpeed);
      }

      // Remove extreme outliers
      prevSpeeds = prevSpeeds.stream()
      .filter(speed -> speed > 0 && speed < 300) // Valid speed range 0-300 km/h
      .collect(Collectors.toList());
      
      final int SPEED_HISTORY_LIMIT = 5;
      if (prevSpeeds.size() >= SPEED_HISTORY_LIMIT) {
         prevSpeeds = prevSpeeds.subList(prevSpeeds.size() - SPEED_HISTORY_LIMIT, prevSpeeds.size());
      }
      prevSpeeds.add(currentSpeed);      

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

      finalAccel = smoothAcceleration(prevSpeeds.get(prevSpeeds.size() - 1), finalAccel, 1.5);

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
  

   private static double smoothAcceleration(double previousAccel, double newAccel, double maxChangePerStep) {
      double change = newAccel - previousAccel;
      if (Math.abs(change) > maxChangePerStep) {
          return previousAccel + Math.signum(change) * maxChangePerStep;
      }
      return newAccel;
  }
  
}
