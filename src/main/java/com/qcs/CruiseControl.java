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

      int lowerBound = set_speed - error;
      int upperBound = set_speed + error;

      if (current_speed >= lowerBound && current_speed <= upperBound) {
         return 0.0; // No acceleration needed
      }

      double requiredChange = set_speed - current_speed;
      double acceleration = Math.min(Math.abs(requiredChange), max_accel);

      // Apply the correct sign for acceleration or deceleration
      return requiredChange > 0 ? acceleration : -acceleration;
   }
}

