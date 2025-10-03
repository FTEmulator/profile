package com.ftemulator.profile.grpc;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import com.ftemulator.profile.entity.User;
import com.ftemulator.profile.grpc.ProfileOuterClass.LoginRequest;
import com.ftemulator.profile.grpc.ProfileOuterClass.LoginResponse;
import com.ftemulator.profile.grpc.ProfileOuterClass.ProfileStatusRequest;
import com.ftemulator.profile.grpc.ProfileOuterClass.ProfileStatusResponse;
import com.ftemulator.profile.grpc.ProfileOuterClass.RegisterUserRequest;
import com.ftemulator.profile.grpc.ProfileOuterClass.RegisterUserResponse;
import com.ftemulator.profile.grpc.ProfileOuterClass.UserRequest;
import com.ftemulator.profile.grpc.ProfileOuterClass.UserResponse;
import com.ftemulator.profile.service.UserService;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@GrpcService
public class UtilsImpl extends ProfileGrpc.ProfileImplBase {

    @Autowired
    private UserService userService;

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

            // Check if user already exists
            User checkUser = null;
            try {
                checkUser = userService.GetByEmail(userData.getEmail());
            } catch (Exception e) {
                // Si no encuentra el usuario, checkUser queda null
                checkUser = null;
            }
            
            if (checkUser != null) {
                responseObserver.onError(
                    Status.ALREADY_EXISTS.withDescription("User already exists")
                                .asRuntimeException()
                );
                return;
            }

            // Encriptar password
            String encrypted = BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt());

            // Crear objeto User y asignar campos
            User user = new User();
            user.setName(userData.getName());
            user.setEmail(userData.getEmail());
            user.setPassword(encrypted);

            if (userData.getCountry() != null && !userData.getCountry().isEmpty()) {
                user.setCountry(userData.getCountry());
            }
            if (userData.getExperience() > 0) {
                user.setExperience(userData.getExperience());
            }
            if (userData.getPhoto() != null && !userData.getPhoto().isEmpty()) {
                user.setPhoto(userData.getPhoto());
            }
            if (userData.getBiography() != null && !userData.getBiography().isEmpty()) {
                user.setBiography(userData.getBiography());
            }

            // Insert uset into database
            userService.createUser(user);

            // Get user
            User createdUser = userService.GetByEmail(userData.getEmail());

            // gRPC response
            RegisterUserResponse response = RegisterUserResponse.newBuilder()
                .setCreated(true)
                .setUserId(createdUser.getId().toString())
                .build();

            // send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                Status.INTERNAL.withDescription("Error al crear usuario: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }


    // Login
    @Override
    public void login(LoginRequest userData, StreamObserver<LoginResponse> responseObserver) {
        try {

            // Compare and get the userId
            LoginResponse userId = userService.login(userData.getEmail(), userData.getPassword());

            // Send response
            responseObserver.onNext(userId);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            responseObserver.onError(
                Status.INTERNAL.withDescription("Error al verificar el usuario: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}