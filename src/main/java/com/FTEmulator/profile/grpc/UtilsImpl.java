package com.FTEmulator.profile.grpc;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

// import com.FTEmulator.profile.entity.User;
import com.FTEmulator.profile.grpc.UtilsOuterClass.ProfileStatusRequest;
import com.FTEmulator.profile.grpc.UtilsOuterClass.ProfileStatusResponse;
// import com.FTEmulator.profile.grpc.UtilsOuterClass.getTestRequest;
// import com.FTEmulator.profile.grpc.UtilsOuterClass.getTestResponse;
// import com.FTEmulator.profile.service.UserTestService;

import io.grpc.stub.StreamObserver;

@GrpcService
public class UtilsImpl extends UtilsGrpc.UtilsImplBase {

    // @Autowired
    // private UserTestService userTestService;

    // Status
    @Override
    public void profileStatus(ProfileStatusRequest request, StreamObserver<ProfileStatusResponse> responseObserver) {
        ProfileStatusResponse response = ProfileStatusResponse.newBuilder().setOk(true).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Get user test
    // @Override
    // public void getUserTest(getTestRequest request, StreamObserver<getTestResponse> responseObserver) {
    //     User userTest = userTestService.getTestByName("test");

    //     getTestResponse response;
    //     if (userTest != null) {
    //         response = getTestResponse.newBuilder()
    //             .setId(userTest.getId().toString())
    //             .setName(userTest.getName())
    //             .setEmail(userTest.getEmail())
    //             .setCountry(userTest.getCountry())
    //             .setStatus("Ok")
    //             .build();
    //     } else {
    //         response = getTestResponse.newBuilder()
    //             .setStatus("No user Found")
    //             .build();
    //     }

    //     responseObserver.onNext(response);
    //     responseObserver.onCompleted();
    // }
}