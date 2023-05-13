package com.example.usermicroservice.grpcService;
import com.example.usermicroservice.mapper.UserMapper;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import communication.userDetailsServiceGrpc;
import communication.UserDetailsResponse;
import communication.Role;

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
    public void register(communication.RegistrationRequest request,
                         io.grpc.stub.StreamObserver<communication.RegistrationResponse> responseObserver) {

    }

}
