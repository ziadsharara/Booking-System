package com.booking.module.booking.mapper;

import com.booking.module.booking.dto.ApproveBookingDTO;
import com.booking.module.booking.dto.CreateBookingDTO;
import com.booking.module.booking.dto.GetBookingDTO;
import com.booking.module.booking.dto.UpdateBookingDTO;
import com.booking.module.booking.entity.Booking;
import com.booking.module.booking.entity.BookingStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

  public Booking toEntity(CreateBookingDTO dto) {
    return Booking.builder()
            .userId(dto.getUserId())
            .resourceId(dto.getResourceId())
            .organizationId(dto.getOrganizationId())
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .status(BookingStatus.INITIAL)  // Always start as INITIAL
            .build();
  }

  public Booking toEntity(UpdateBookingDTO dto) {
    return Booking.builder()
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .build();
  }

  public GetBookingDTO toGetBookingDTO(Booking booking) {
    return GetBookingDTO.builder()
            .id(booking.getId())
            .userId(booking.getUserId())
            .resourceId(booking.getResourceId())
            .organizationId(booking.getOrganizationId())
            .startTime(booking.getStartTime())
            .endTime(booking.getEndTime())
            .status(booking.getStatus())
            .approvedBy(booking.getApprovedBy())
            .approvedAt(booking.getApprovedAt())
            .completedAt(booking.getCompletedAt())
            .cancelledAt(booking.getCancelledAt())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
  }

  public List<GetBookingDTO> toGetBookingDTOList(List<Booking> bookings) {
    List<GetBookingDTO> dtos = new ArrayList<>();
    for (Booking booking : bookings) {
      dtos.add(toGetBookingDTO(booking));
    }
    return dtos;
  }
}