package com.FTEmulator.profile.grpc;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import com.FTEmulator.profile.entity.User;
import com.FTEmulator.profile.grpc.ProfileOuterClass.ProfileStatusRequest;
import com.FTEmulator.profile.grpc.ProfileOuterClass.ProfileStatusResponse;
import com.FTEmulator.profile.grpc.ProfileOuterClass.RegisterUserRequest;
import com.FTEmulator.profile.grpc.ProfileOuterClass.RegisterUserResponse;
import com.FTEmulator.profile.grpc.ProfileOuterClass.UserRequest;
import com.FTEmulator.profile.grpc.ProfileOuterClass.UserResponse;
import com.FTEmulator.profile.service.UserService;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@GrpcService
public class UtilsImpl extends ProfileGrpc.ProfileImplBase {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StringEncryptor stringEncryptor;

    // Status
    @Override
    public void profileStatus(ProfileStatusRequest request, StreamObserver<ProfileStatusResponse> responseObserver) {
        ProfileStatusResponse response = ProfileStatusResponse.newBuilder().setOk(true).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Get user
    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        
        try {
            // Get id
            UUID userId = UUID.fromString(request.getUserId());

            // Get user
            User user = userService.getById(userId);

            // Set response
            UserResponse response = UserResponse.newBuilder()
                .setUserId(user.getId().toString())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .build();

            // Sen response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            // UUID mal formado
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Formato de UUID inválido: " + request.getUserId())
                    .asRuntimeException()
            );
        } catch (NoSuchElementException e) {
            // Usuario no encontrado
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Usuario no encontrado con ID: " + request.getUserId())
                    .asRuntimeException()
            );
        } catch (Exception e) {
            // Otros errores
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Error interno: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    // Register user
    @Override
    public void createUser(RegisterUserRequest userData, StreamObserver<RegisterUserResponse> responseObserver) {
        try {
            // Encript password
            String encripted = stringEncryptor.encrypt(userData.getPassword());

            // Set data
            User user = new User();
            user.setName(userData.getName());
            user.setEmail(userData.getEmail());
            user.setPassword(encripted);
            user.setCountry(userData.getCountry());
            if (!userData.getCountry().isEmpty()) user.setCountry(userData.getCountry());
            if (userData.getExperience() > 0) user.setExperience(userData.getExperience());
            if (!userData.getPhoto().isEmpty()) user.setPhoto(userData.getPhoto());
            if (!userData.getBiography().isEmpty()) user.setBiography(userData.getBiography());

            // Insert user to database
            userService.createUser(user);

            // Return 201 status (created)
            RegisterUserResponse response = RegisterUserResponse.newBuilder().setCreated(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                Status.INTERNAL.withDescription("Error al crear usuario: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}