package com.booking.module.booking.controller;

import com.booking.module.booking.dto.ApproveBookingDTO;
import com.booking.module.booking.dto.CreateBookingDTO;
import com.booking.module.booking.dto.GetBookingDTO;
import com.booking.module.booking.dto.UpdateBookingDTO;
import com.booking.module.booking.entity.Booking;
import com.booking.module.booking.entity.BookingStatus;
import com.booking.module.booking.mapper.BookingMapper;
import com.booking.module.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;
  private final BookingMapper bookingMapper;

  // =========== Create ===========
  @PostMapping
  public ResponseEntity<GetBookingDTO> createBooking(@Valid @RequestBody CreateBookingDTO dto) {
    try {
      Booking booking = bookingService.createBooking(dto);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // =========== Read ===========
  @GetMapping("/{id}")
  public ResponseEntity<GetBookingDTO> getBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.getBookingById(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }


  @GetMapping("/user/{userId}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByUser(@PathVariable Long userId) {
    List<Booking> bookings = bookingService.getBookingsByUserId(userId);
    List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/resource/{resourceId}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByResource(@PathVariable Long resourceId) {
    List<Booking> bookings = bookingService.getBookingsByResourceId(resourceId);
    List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
    return ResponseEntity.ok(dtos);
  }


  @GetMapping("/status/{status}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
    List<Booking> bookings = bookingService.getBookingsByStatus(status);
    List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/in-progress")
  public ResponseEntity<List<GetBookingDTO>> getInProgressBookings() {
    List<Booking> bookings = bookingService.getInProgressBookings();
    List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/user/{userId}/pending")
  public ResponseEntity<List<GetBookingDTO>> getUserPendingBookings(@PathVariable Long userId) {
    List<Booking> bookings = bookingService.getUserPendingBookings(userId);
    List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
    return ResponseEntity.ok(dtos);
  }

  // =========== Status Transactions ===========
  // Start booking (APPROVED → IN_PROGRESS)
  @PutMapping("/{id}/approve")
  public ResponseEntity<GetBookingDTO> approveBooking(
          @PathVariable Long id,
          @Valid @RequestBody ApproveBookingDTO dto) {
    try {
      Booking booking = bookingService.approveBooking(id, dto);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // Complete booking (IN_PROGRESS → COMPLETED)
  @PutMapping("/{id}/start")
  public ResponseEntity<GetBookingDTO> startBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.startBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // Complete booking (IN_PROGRESS → COMPLETED)
  @PutMapping("/{id}/complete")
  public ResponseEntity<GetBookingDTO> completeBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.completeBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // Cancel booking (can cancel from INITIAL or APPROVED)
  @PutMapping("/{id}/cancel")
  public ResponseEntity<GetBookingDTO> cancelBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.cancelBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // ========== DELETE ==========
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    try {
      bookingService.deleteBooking(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
