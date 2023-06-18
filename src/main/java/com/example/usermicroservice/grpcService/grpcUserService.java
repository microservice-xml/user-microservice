package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.service.UserService;
import communication.EmptyMessage;
import communication.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class grpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(grpcUserService.class);

    @Override
    public void incPenalties(communication.LongId request,
                             io.grpc.stub.StreamObserver<communication.EmptyMessage> responseObserver) {
        logger.trace("Request to add a penalty for the user with id {} was made", request.getId());
        userService.incPenalties(request.getId());
        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }
}
