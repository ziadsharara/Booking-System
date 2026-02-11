package com.booking.module.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  @Column(name = "approved_at")
  private LocalDateTime approvedAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  @Column(nullable = false, length = 15)
  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "resource_id", nullable = false)
  private Long resourceId;

  @Column(name = "organization_id", nullable = false)
  private Long organizationId;

  @Column(name = "approved_by")
  private Long approvedBy;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}