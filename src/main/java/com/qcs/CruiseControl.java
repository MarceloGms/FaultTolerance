package com.qcs;

import java.util.List;

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
  
}
