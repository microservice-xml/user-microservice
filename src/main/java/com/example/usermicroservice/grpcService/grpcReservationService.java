package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.service.ReservationService;
import communication.EmptyMessage;
import communication.ReservationServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class grpcReservationService extends ReservationServiceGrpc.ReservationServiceImplBase {
    private final ReservationService reservationService;
    private Logger logger = LoggerFactory.getLogger(grpcReservationService.class);

    @Override
    public void calculateIsHighlighted(communication.LongId request,
                                       io.grpc.stub.StreamObserver<communication.EmptyMessage> responseObserver) {
        logger.trace("Request to calculate if user with id {} is highlighted was made", request.getId());
        reservationService.updateHostHighlighted(request.getId());
        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }
}
