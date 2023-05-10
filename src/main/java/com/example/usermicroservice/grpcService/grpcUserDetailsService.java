package com.example.usermicroservice.grpcService;
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
        UserDetailsResponse response = UserDetailsResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setRole(user.getRole().equals(com.example.usermicroservice.model.enums.Role.GUEST) ?  Role.GUEST: Role.HOST)
                .setPenalties(user.getPenalties()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
