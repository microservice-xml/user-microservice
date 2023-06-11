package com.example.usermicroservice.grpcService;
import com.example.usermicroservice.mapper.UserMapper;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import communication.MessageResponse;
import communication.UserDetailsResponse;
import communication.UserList;
import communication.userDetailsServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import static com.example.usermicroservice.mapper.UserMapper.*;

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
            response = MessageResponse.newBuilder().setMessage("User already exists.").build();

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
                        io.grpc.stub.StreamObserver<UserList> responseObserver) {
                List<User> finalUsers = userService.findAll();
                UserList userList = UserList.newBuilder().addAllUsers(convertUsersToUsersExtendedGrpc(finalUsers)).build();
                responseObserver.onNext(userList);
                responseObserver.onCompleted();
    }

    @Override
    public void getById(communication.UserIdRequest request,
                        io.grpc.stub.StreamObserver<communication.RegisterUserAvgGrade> responseObserver) {
        User user = userService.getById(request.getId());
        responseObserver.onNext(UserMapper.convertFromUserToRegisterUserAvgGrade(user));
        responseObserver.onCompleted();
    }

    @Override
    public void delete(communication.UserIdRequest request,
                       io.grpc.stub.StreamObserver<communication.MessageResponse> responseObserver) {
                    boolean success = userService.deleteUser(request.getId());
                    MessageResponse message;
                    if(success) {
                        message = MessageResponse.newBuilder().setMessage("You have successfully deleted your profile.").build();
                    } else {
                        message = MessageResponse.newBuilder().setMessage("You profile can't be deleted due to future reservations.").build();
                    }
                    responseObserver.onNext(message);
                    responseObserver.onCompleted();
    }

}
