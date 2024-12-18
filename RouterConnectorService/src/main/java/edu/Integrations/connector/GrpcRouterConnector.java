package edu.Integrations.connector;


import edu.Integrations.router.enteties.RouterConnector;
import edu.Integrations.router.enteties.TestRouterConnector;
import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;
import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;
import proto.RouterConnectorGrpc;
import proto.RouterProtos;

import java.util.logging.Logger;

public class GrpcRouterConnector extends RouterConnectorGrpc.RouterConnectorImplBase implements BindableService {
    private static final String envForProduction = System.getenv("ROUTER_BEHAVIOUR");


    @Override
    public void initialisationSecret(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        Logger.getAnonymousLogger().info("Doing response to initialisationSecret");
        ClientDtoToRouter clientTransfer = new ClientDtoToRouter(request);
        String result = "";
        if (envForProduction.equals("test")) {
            Logger.getAnonymousLogger().info("Doing response to initialisationSecret in test");
            result = TestRouterConnector.initialisationSecret(clientTransfer);
        } else {
            Logger.getAnonymousLogger().info("Doing response to initialisationSecret in production");
            result = RouterConnector.initialisationSecret(clientTransfer);
        }

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void initialisationTrial(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        ClientDtoToRouter clientTransfer = new ClientDtoToRouter(request);
        String result = "";
        if (envForProduction.equals("test")) {
            result = TestRouterConnector.initialisationTrial(clientTransfer);
        } else {
            result = RouterConnector.initialisationTrial(clientTransfer);
        }

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void prolongSecret(RouterProtos.ClientRequestWithProlongationSecret request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        ClientDtoToRouterWithVpnProfile clientTransfer = new ClientDtoToRouterWithVpnProfile(request);
        String result = "";
        if (envForProduction.equals("test")) {
            result = TestRouterConnector.prolongSecret(clientTransfer);
        } else {
            result = RouterConnector.prolongSecret(clientTransfer);
        }

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}