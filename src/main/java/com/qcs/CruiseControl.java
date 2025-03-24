package com.qcs;

import java.util.List;

public class CruiseControl {
   private int set_speed;
   private int error;
   private int max_accel;
   private int current_speed;
   private List<Integer> prev_speed;

   public CruiseControl(int set_speed, int error, int max_accel, int current_speed, List<Integer> prev_speed) {
      this.set_speed = set_speed;
      this.error = error;
      this.max_accel = max_accel;
      this.current_speed = current_speed;
      this.prev_speed = prev_speed;
   }

   public double calculateAcceleration() {
      prev_speed.add(current_speed);

      // Calculate the average speed
      double sum = 0;
      for (int speed : prev_speed) {
         sum += speed;
      }
      double average_speed = sum / prev_speed.size();

      int lowerBound = set_speed - error;
      int upperBound = set_speed + error;

      // If the average speed is within the error bounds, return 0
      if (average_speed >= lowerBound && average_speed <= upperBound) {
         return 0.0;
      }

      // If not within the error bounds, calculate the required change in speed
      double requiredChange = set_speed - current_speed;
      double acceleration = Math.min(Math.abs(requiredChange), max_accel);

      return requiredChange > 0 ? acceleration : -acceleration;
   }
}

