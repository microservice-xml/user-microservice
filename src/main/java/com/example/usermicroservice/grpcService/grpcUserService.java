package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.service.UserService;
import communication.EmptyMessage;
import communication.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class grpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    @Override
    public void incPenalties(communication.LongId request,
                             io.grpc.stub.StreamObserver<communication.EmptyMessage> responseObserver) {

        userService.incPenalties(request.getId());
        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }
}
