package com.booking.module.booking.repository;

import com.booking.module.booking.entity.Booking;
import com.booking.module.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  // SELECT * FROM resources WHEN status = ?
  Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

  // User Bookings
  Page<Booking> findAllByUserId(Long userId, Pageable pageable);
  Page<Booking> findByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

  // Resource Bookings
  Page<Booking> findAllByResourceId(Long resourceId, Pageable pageable);
  Page<Booking> findByResourceIdAndStatus(Long resourceId, BookingStatus status, Pageable pageable);

}
