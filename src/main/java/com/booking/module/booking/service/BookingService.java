package com.booking.module.booking.service;

import com.booking.module.booking.dto.ApproveBookingDTO;
import com.booking.module.booking.dto.CreateBookingDTO;
import com.booking.module.booking.entity.Booking;
import com.booking.module.booking.entity.BookingStatus;
import com.booking.module.booking.mapper.BookingMapper;
import com.booking.module.booking.repository.BookingRepository;
import com.booking.module.resource.service.ResourceService;
import com.booking.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;
  private final UserService userService;
  private final ResourceService resourceService;

  // =========== Create ===========
  @Transactional
  public Booking createBooking(CreateBookingDTO dto) {

    // Validate on end time is after start time
    if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().isEqual(dto.getStartTime())) {
      throw new IllegalArgumentException("End time must be after start time");
    }

    // Validate user exists
    userService.getUser(dto.getUserId());

    // Validate resource exists and is ENABLED
    if (!resourceService.isResourceAvailable(dto.getResourceId())) {
      throw new IllegalArgumentException("Resource is not available - already booked or disabled");
    }

    // Create booking
    Booking booking = bookingMapper.toEntity(dto);
    Booking savedBooking = bookingRepository.save(booking);

    // Disable resource after booking
    resourceService.disableResource(dto.getResourceId());

    return savedBooking;
  }

  // =========== Read ===========
  public Booking getBookingById(Long id) {
    return bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  public List<Booking> getBookingsByUserId(Long userId) {
    return bookingRepository.findAllByUserId(userId);
  }

  public List<Booking> getBookingsByResourceId(Long resourceId) {
    return bookingRepository.findAllByResourceId(resourceId);
  }


  public List<Booking> getBookingsByStatus(BookingStatus status) {
    return bookingRepository.findAllByStatus(status);
  }

  public List<Booking> getInProgressBookings() {
    return bookingRepository.findAllByStatus(BookingStatus.IN_PROGRESS);
  }

  public List<Booking> getUserPendingBookings(Long userId) {
    return bookingRepository.findAllByUserIdAndStatus(userId, BookingStatus.INITIAL);
  }

  // =========== Status Transactions ===========
  // Approve booking (INITIAL → APPROVED)
  @Transactional
  public Booking approveBooking(Long id, ApproveBookingDTO dto) {
    Booking booking = getBookingById(id);

    if (booking.getStatus() != BookingStatus.INITIAL) {
      throw new IllegalArgumentException("Only INITIAL bookings can be APPROVED");
    }

    // Verify approved by exist
    userService.getUser(dto.getApprovedByUserId());

    booking.setStatus(BookingStatus.APPROVED);
    booking.setApprovedBy(dto.getApprovedByUserId());
    booking.setApprovedAt(LocalDateTime.now());

    return bookingRepository.save(booking);
  }

  // Start booking (APPROVED → IN_PROGRESS)
  @Transactional
  public Booking startBooking(Long id) {
    Booking booking = getBookingById(id);

    if (booking.getStatus() != BookingStatus.APPROVED) {
      throw new IllegalArgumentException("Only APPROVED bookings can be started");
    }

    booking.setStatus(BookingStatus.IN_PROGRESS);
    return bookingRepository.save(booking);
  }

  // Complete booking (IN_PROGRESS → COMPLETED)
  @Transactional
  public Booking completeBooking(Long id) {
    Booking booking = getBookingById(id);

    if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
      throw new IllegalArgumentException("Only IN_PROGRESS bookings can be completed");
    }

    booking.setStatus(BookingStatus.COMPLETED);
    booking.setCompletedAt(LocalDateTime.now());
    bookingRepository.save(booking);

    resourceService.enableResource(booking.getResourceId());

    return booking;
  }

  // Cancel booking (can cancel from INITIAL or APPROVED)
  @Transactional
  public Booking cancelBooking(Long id) {
    Booking booking = getBookingById(id);

    if (booking.getStatus() == BookingStatus.IN_PROGRESS || booking.getStatus() == BookingStatus.COMPLETED) {
      throw new IllegalArgumentException("Cannot cancel " + booking.getStatus() + " bookings");
    }

    if (booking.getStatus() == BookingStatus.CANCELLED) {
      throw new IllegalArgumentException("Booking is already cancelled");
    }

    booking.setStatus(BookingStatus.CANCELLED);
    booking.setCancelledAt(LocalDateTime.now());
    bookingRepository.save(booking);

    resourceService.enableResource(booking.getResourceId());

    return booking;
  }

  // =========== Delete ===========
  public void deleteBooking(Long id) {
    Booking booking = getBookingById(id);

    if (booking.getStatus() != BookingStatus.COMPLETED && booking.getStatus() != BookingStatus.CANCELLED) {
      // Re-enable resource
      resourceService.enableResource(booking.getResourceId());
    }

    bookingRepository.deleteById(id);
  }
}
