package com.booking.module.booking.entity;

public enum BookingStatus {
  INITIAL,        // Just created, waiting for approval
  APPROVED,       // Manager approved, time slot reserved
  IN_PROGRESS,    // Currently being used
  COMPLETED,      // Finished
  CANCELLED       // Cancelled by employee or manager
}
