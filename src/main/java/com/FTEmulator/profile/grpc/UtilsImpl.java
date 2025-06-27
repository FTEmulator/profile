package com.FTEmulator.profile.grpc;

import org.springframework.grpc.server.service.GrpcService;

import com.FTEmulator.profile.grpc.UtilsOuterClass.ProfileStatusRequest;
import com.FTEmulator.profile.grpc.UtilsOuterClass.ProfileStatusResponse;
import com.FTEmulator.profile.grpc.UtilsOuterClass.getTestRequest;
import com.FTEmulator.profile.grpc.UtilsOuterClass.getTestResponse;

import io.grpc.stub.StreamObserver;

@GrpcService
public class UtilsImpl extends UtilsGrpc.UtilsImplBase {

    // Status
    @Override
    public void profileStatus(ProfileStatusRequest request, StreamObserver<ProfileStatusResponse> responseObserver) {
        ProfileStatusResponse response = ProfileStatusResponse.newBuilder().setOk(true).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Get user test
    public void GetUserTest(getTestRequest request, StreamObserver<getTestResponse> responseObserver) {
        // db connect

        getTestResponse response = getTestResponse.newBuilder().setData(null).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}