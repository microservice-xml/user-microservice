package com.example.usermicroservice.grpcService;
import com.example.usermicroservice.mapper.UserMapper;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import communication.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import static com.example.usermicroservice.mapper.UserMapper.convertUserToUserGrpc;
import static com.example.usermicroservice.mapper.UserMapper.convertUsersToUsersGrpc;

@GrpcService
@RequiredArgsConstructor
public class grpcUserDetailsService extends userDetailsServiceGrpc.userDetailsServiceImplBase {

    private final UserService userService;

    @Override
    public void getUserDetails(communication.UserDetailsRequest request,
                               io.grpc.stub.StreamObserver<communication.UserDetailsResponse> responseObserver){
        User user = userService.loadUserByUsername(request.getUsername());
        UserDetailsResponse response = UserMapper.convertUserToUserDetailsResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void register(communication.RegisterUser request,
                         io.grpc.stub.StreamObserver<communication.MessageResponse> responseObserver) {

        User user = UserMapper.covertRegisterRequestToEntity(request);
        User u = userService.registerUser(user);
        MessageResponse response;
        if(u!=null)
            response = MessageResponse.newBuilder().setMessage("User created.").build();
        else
            response = MessageResponse.newBuilder().setMessage("Register failed.").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void changeUserInfo(communication.User request,
                               io.grpc.stub.StreamObserver<communication.User> responseObserver) {
                User user = UserMapper.convertUserGrpcToUser(request);
                User finalUser = userService.changeUserInfo(user);

                responseObserver.onNext(convertUserToUserGrpc(finalUser));
                responseObserver.onCompleted();
    }

    @Override
    public void findAll(communication.EmptyRequest request,
                        io.grpc.stub.StreamObserver<communication.UserList> responseObserver) {
                List<User> finalUsers = userService.findAll();
                UserList userList = UserList.newBuilder().addAllUsers(convertUsersToUsersGrpc(finalUsers)).build();
                responseObserver.onNext(userList);
                responseObserver.onCompleted();
    }
}
