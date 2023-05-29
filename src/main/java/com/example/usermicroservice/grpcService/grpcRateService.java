package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.mapper.RateMapper;
import com.example.usermicroservice.model.Rate;
import com.example.usermicroservice.repository.RateRepository;
import com.example.usermicroservice.service.RateService;
import com.example.usermicroservice.service.UserService;
import communication.MessageResponse;
import communication.rateServiceGrpc;
import communication.userDetailsServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import static com.example.usermicroservice.mapper.RateMapper.*;
import static com.example.usermicroservice.mapper.UserMapper.convertUserToUserGrpc;

@GrpcService
@RequiredArgsConstructor
public class grpcRateService extends rateServiceGrpc.rateServiceImplBase {
    private final RateService rateService;

    private final RateRepository rateRepository;

    @Override
    public void rateHost(communication.Rate request,
                         io.grpc.stub.StreamObserver<communication.MessageResponse> responseObserver) {
        Rate rate = convertRateRequestToEntity(request);
        Rate r = rateService.rateHost(rate);

        MessageResponse response;
        if(r!=null)
            response = MessageResponse.newBuilder().setMessage("Rate added.").build();
        else
            response = MessageResponse.newBuilder().setMessage("Adding rate failed.").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void changeRate(communication.Rate request,
                           io.grpc.stub.StreamObserver<communication.Rate> responseObserver) {
        Rate rate = convertRateRequestToEntityWithId(request);
        Rate r = rateService.changeRate(rate);

        responseObserver.onNext(convertFromMessageToRateWithId(r));
        responseObserver.onCompleted();
    }
    @Override
    public void deleteRate(communication.UserIdRequest request,
                           io.grpc.stub.StreamObserver<communication.Rate> responseObserver) {

        Rate rate = rateService.deleteRate(request.getId());

        responseObserver.onNext(convertFromMessageToRateWithId(rate));
        responseObserver.onCompleted();
    }
}
