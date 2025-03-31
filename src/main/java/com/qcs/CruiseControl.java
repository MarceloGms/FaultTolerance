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


   public static double faultToleranceCalcAccel(int setSpeed, int error, int maxAccel, int currentSpeed, List<Integer> prevSpeeds) {
      // Validate parameters
      if (maxAccel <= 0) {
         throw new IllegalArgumentException("Max acceleration must be non-negative.");
      }
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
}
