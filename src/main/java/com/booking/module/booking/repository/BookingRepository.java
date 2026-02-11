package com.booking.module.booking.repository;

import com.booking.module.booking.entity.Booking;
import com.booking.module.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  // Find bookings by user
  List<Booking> findAllByUserId(Long userId);

  // Find bookings by resource
  List<Booking> findAllByResourceId(Long resourceId);

  // Find bookings by status
  List<Booking> findAllByStatus(BookingStatus status);

  // Find bookings by user and status
  List<Booking> findAllByUserIdAndStatus(Long userId, BookingStatus status);

  // Find bookings by resource and status
  List<Booking> findAllByResourceIdAndStatus(Long resourceId, BookingStatus status);

}
