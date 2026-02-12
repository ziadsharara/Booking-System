package com.booking.module.booking.controller;

import com.booking.module.auth.util.JwtUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;
  private final BookingMapper bookingMapper;
  private final JwtUtil jwtUtil;

  // Extract organizationId from JWT token
  private Long getOrganizationIdFromToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String token = (String) auth.getCredentials();
    Long organizationId = jwtUtil.getOrganizationIdFromToken(token);

    if (organizationId == null) {
      throw new IllegalArgumentException("Organization ID not found in token");
    }

    return organizationId;
  }

  // Extract userId from JWT token
  private Long getUserIdFromToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String token = (String) auth.getCredentials();
    return jwtUtil.getUserIdFromToken(token);
  }

  // Check if user is EMPLOYEE
  private boolean isEmployee() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
  }

  // =========== Create ===========
  // EMPLOYEE can create booking
  @PreAuthorize("hasRole('EMPLOYEE')")
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
  // MANAGER can see all bookings, EMPLOYEE can only see his own
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @GetMapping("/{id}")
  public ResponseEntity<GetBookingDTO> getBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.getBookingById(id);

      // EMPLOYEE can only see his own booking
      if (isEmployee()) {
        Long currentUserId = getUserIdFromToken();
        if (!currentUserId.equals(booking.getUserId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      } else {
        // MANAGER can see bookings from his org only
        Long organizationId = getOrganizationIdFromToken();
        if (!organizationId.equals(booking.getOrganizationId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }

      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(booking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // MANAGER can see all bookings by user in his org
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByUser(@PathVariable Long userId) {
    try {
      Long organizationId = getOrganizationIdFromToken();

      List<Booking> bookings = bookingService.getBookingsByUserId(userId);
      // Filter to only show bookings from manager's org
      bookings = bookings.stream()
              .filter(b -> organizationId.equals(b.getOrganizationId()))
              .toList();

      List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // MANAGER can see all bookings for resource in his org
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/resource/{resourceId}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByResource(@PathVariable Long resourceId) {
    try {
      Long organizationId = getOrganizationIdFromToken();

      List<Booking> bookings = bookingService.getBookingsByResourceId(resourceId);
      // Filter to only show bookings from manager's org
      bookings = bookings.stream()
              .filter(b -> organizationId.equals(b.getOrganizationId()))
              .toList();

      List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // MANAGER can see all bookings by status in his org
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/status/{status}")
  public ResponseEntity<List<GetBookingDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
    try {
      Long organizationId = getOrganizationIdFromToken();

      List<Booking> bookings = bookingService.getBookingsByStatus(status);
      // Filter to only show bookings from manager's org
      bookings = bookings.stream()
              .filter(b -> organizationId.equals(b.getOrganizationId()))
              .toList();

      List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // MANAGER can see all in-progress bookings in his org
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/in-progress")
  public ResponseEntity<List<GetBookingDTO>> getInProgressBookings() {
    try {
      Long organizationId = getOrganizationIdFromToken();

      List<Booking> bookings = bookingService.getInProgressBookings();
      // Filter to only show bookings from manager's org
      bookings = bookings.stream()
              .filter(b -> organizationId.equals(b.getOrganizationId()))
              .toList();

      List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // MANAGER can see all pending bookings for user in his org, EMPLOYEE can see his own
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @GetMapping("/user/{userId}/pending")
  public ResponseEntity<List<GetBookingDTO>> getUserPendingBookings(@PathVariable Long userId) {
    try {
      // EMPLOYEE can only see his own pending bookings
      if (isEmployee()) {
        Long currentUserId = getUserIdFromToken();
        if (!currentUserId.equals(userId)) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      } else {
        // MANAGER can see pending bookings for users in his org only
        Long organizationId = getOrganizationIdFromToken();
        List<Booking> userBookings = bookingService.getBookingsByUserId(userId);
        if (!userBookings.isEmpty() && !organizationId.equals(userBookings.get(0).getOrganizationId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }

      List<Booking> bookings = bookingService.getUserPendingBookings(userId);
      List<GetBookingDTO> dtos = bookingMapper.toGetBookingDTOList(bookings);
      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // =========== Status Transactions ===========
  // MANAGER approve booking
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}/approve")
  public ResponseEntity<GetBookingDTO> approveBooking(
          @PathVariable Long id,
          @Valid @RequestBody ApproveBookingDTO dto) {
    try {
      Long organizationId = getOrganizationIdFromToken();
      Booking booking = bookingService.getBookingById(id);

      // Ensure booking is from manager's org
      if (!organizationId.equals(booking.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      Booking approvedBooking = bookingService.approveBooking(id, dto);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(approvedBooking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // MANAGER start booking
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}/start")
  public ResponseEntity<GetBookingDTO> startBooking(@PathVariable Long id) {
    try {
      Long organizationId = getOrganizationIdFromToken();
      Booking booking = bookingService.getBookingById(id);

      // Ensure booking is from manager's org
      if (!organizationId.equals(booking.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      Booking startedBooking = bookingService.startBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(startedBooking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // MANAGER complete booking
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}/complete")
  public ResponseEntity<GetBookingDTO> completeBooking(@PathVariable Long id) {
    try {
      Long organizationId = getOrganizationIdFromToken();
      Booking booking = bookingService.getBookingById(id);

      // Ensure booking is from manager's org
      if (!organizationId.equals(booking.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      Booking completedBooking = bookingService.completeBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(completedBooking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // MANAGER and EMPLOYEE can cancel if INITIAL status
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @PutMapping("/{id}/cancel")
  public ResponseEntity<GetBookingDTO> cancelBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.getBookingById(id);

      // Check authorization
      if (isEmployee()) {
        // EMPLOYEE can only cancel his own booking
        Long currentUserId = getUserIdFromToken();
        if (!currentUserId.equals(booking.getUserId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      } else {
        // MANAGER can cancel bookings from his org only
        Long organizationId = getOrganizationIdFromToken();
        if (!organizationId.equals(booking.getOrganizationId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }

      Booking cancelledBooking = bookingService.cancelBooking(id);
      GetBookingDTO responseDTO = bookingMapper.toGetBookingDTO(cancelledBooking);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // ========== DELETE ==========
  // MANAGER and EMPLOYEE can delete
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    try {
      Booking booking = bookingService.getBookingById(id);

      // Check authorization
      if (isEmployee()) {
        // EMPLOYEE can only delete his own booking
        Long currentUserId = getUserIdFromToken();
        if (!currentUserId.equals(booking.getUserId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      } else {
        // MANAGER can delete bookings from his org only
        Long organizationId = getOrganizationIdFromToken();
        if (!organizationId.equals(booking.getOrganizationId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }

      bookingService.deleteBooking(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}