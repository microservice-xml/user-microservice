package com.example.usermicroservice.grpcService;

import com.example.usermicroservice.mapper.RateMapper;
import com.example.usermicroservice.model.Rate;
import com.example.usermicroservice.repository.RateRepository;
import com.example.usermicroservice.service.RateService;
import com.example.usermicroservice.service.UserService;
import communication.MessageResponse;
import communication.UserIdRequest;
import communication.rateServiceGrpc;
import communication.userDetailsServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.usermicroservice.mapper.RateMapper.*;
import static com.example.usermicroservice.mapper.UserMapper.convertUserToUserGrpc;

@GrpcService
@RequiredArgsConstructor
public class grpcRateService extends rateServiceGrpc.rateServiceImplBase {
    private final RateService rateService;

    private final RateRepository rateRepository;

    private Logger logger = LoggerFactory.getLogger(grpcRateService.class);

    @Override
    public void rateHost(communication.Rate request,
                         io.grpc.stub.StreamObserver<communication.MessageResponse> responseObserver) {
        logger.trace("Request to rate the host with id {} with grade {} was made", request.getHostId(), request.getRateValue());
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
        logger.trace("Request to edit the rating for host with id {} with new grade {} was made", request.getHostId(), request.getRateValue());
        Rate rate = convertRateRequestToEntityWithId(request);
        Rate r = rateService.changeRate(rate);

        responseObserver.onNext(convertFromMessageToRateWithId(r));
        responseObserver.onCompleted();
    }
    @Override
    public void deleteRate(communication.UserIdRequest request,
                           io.grpc.stub.StreamObserver<communication.Rate> responseObserver) {
        logger.trace("Request to delete the host rating with id {} was made", request.getId());
        Rate rate = rateService.deleteRate(request.getId());

        responseObserver.onNext(convertFromMessageToRateWithId(rate));
        responseObserver.onCompleted();
    }
    @Override
    public void getAllByHostId(communication.UserIdRequest request,
                               io.grpc.stub.StreamObserver<communication.ListRate> responseObserver) {
        logger.trace("Request to find all ratings for host with id {} was made", request.getId());
        List<Rate> rates = rateService.getAllByHostId(request.getId());

        List<communication.Rate> convertedRates = RateMapper.convertListFromMessageToRateWithId(rates);
        communication.ListRate.Builder listRateResponseBuilder = communication.ListRate.newBuilder();
        listRateResponseBuilder.addAllRates(convertedRates);
        communication.ListRate listRateResponse = listRateResponseBuilder.build();

        responseObserver.onNext(listRateResponse);
        responseObserver.onCompleted();
    }
}
