package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.service.ReservationService;
import communication.EmptyMessage;
import communication.ReservationServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class grpcReservationService extends ReservationServiceGrpc.ReservationServiceImplBase {
    private final ReservationService reservationService;

    @Override
    public void calculateIsHighlighted(communication.LongId request,
                                       io.grpc.stub.StreamObserver<communication.EmptyMessage> responseObserver) {

        reservationService.updateHostHighlighted(request.getId());
        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }
}
