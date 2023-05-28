package com.example.usermicroservice.mapper;

import communication.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.usermicroservice.mapper.LocalDateMapper.convertGoogleTimestampToLocalDate;
import static com.example.usermicroservice.mapper.LocalDateMapper.convertLocalDateToGoogleTimestamp;
import static com.example.usermicroservice.mapper.ReservationStatusMapper.convertReservationStatusGrpcToReservationStatus;
import static com.example.usermicroservice.mapper.ReservationStatusMapper.convertReservationStatusToReservationStatusGrpc;


@Component
@RequiredArgsConstructor
public class ReservationMapper {

    public static com.example.usermicroservice.model.Reservation convertReservationGrpcToReservation(Reservation reservation) {
        return com.example.usermicroservice.model.Reservation.builder()
                .id(reservation.getId())
                .status(convertReservationStatusGrpcToReservationStatus(reservation.getStatus()))
                .start(convertGoogleTimestampToLocalDate(reservation.getStart()))
                .end(convertGoogleTimestampToLocalDate(reservation.getEnd()))
                .userId(reservation.getUserId())
                .slotId(reservation.getSlotId())
                .numberOfGuests(reservation.getNumberOfGuests())
                .hostId(reservation.getHostId())
                .build();
    }

    public static Reservation convertReservationToReservationGrpc(com.example.usermicroservice.model.Reservation reservation){
        return Reservation.newBuilder()
                .setId(reservation.getId())
                .setUserId(reservation.getUserId())
                .setSlotId(reservation.getSlotId())
                .setNumberOfGuests(reservation.getNumberOfGuests())
                .setStart(convertLocalDateToGoogleTimestamp(reservation.getStart()))
                .setEnd(convertLocalDateToGoogleTimestamp(reservation.getEnd()))
                .setStatus(convertReservationStatusToReservationStatusGrpc(reservation.getStatus()))
                .setHostId(reservation.getHostId())
                .build();
    }
}
