package com.example.usermicroservice.mapper;

import communication.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusMapper {
    public static com.example.usermicroservice.model.enums.ReservationStatus convertReservationStatusGrpcToReservationStatus(ReservationStatus status){
        if(status.equals(ReservationStatus.ACCEPTED))
            return com.example.usermicroservice.model.enums.ReservationStatus.ACCEPTED;
        else if(status.equals(ReservationStatus.PENDING))
            return com.example.usermicroservice.model.enums.ReservationStatus.PENDING;
        else if(status.equals(ReservationStatus.DECLINED))
            return com.example.usermicroservice.model.enums.ReservationStatus.DECLINED;
        else return com.example.usermicroservice.model.enums.ReservationStatus.CANCELED;

    }

    public static ReservationStatus convertReservationStatusToReservationStatusGrpc(com.example.usermicroservice.model.enums.ReservationStatus status){
        if(status.equals(com.example.usermicroservice.model.enums.ReservationStatus.ACCEPTED))
            return ReservationStatus.ACCEPTED;
        else if(status.equals(com.example.usermicroservice.model.enums.ReservationStatus.PENDING))
            return ReservationStatus.PENDING;
        else if(status.equals(com.example.usermicroservice.model.enums.ReservationStatus.DECLINED))
            return ReservationStatus.DECLINED;
        else
            return ReservationStatus.CANCELED;

    }
}
