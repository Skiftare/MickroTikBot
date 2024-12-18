package edu.Integrations.connector;


import edu.Integrations.router.VpnProfileServerManagerFactory;
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

    private final VpnProfileServerManagerFactory vpnManagerFactory = new VpnProfileServerManagerFactory();




    @Override
    public void initialisationSecret(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        Logger.getAnonymousLogger().info("Doing response to initialisationSecret");
        ClientDtoToRouter clientTransfer = new ClientDtoToRouter(request);
        String result = vpnManagerFactory.initialisationSecret(clientTransfer);

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void initialisationTrial(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        ClientDtoToRouter clientTransfer = new ClientDtoToRouter(request);
        String result = vpnManagerFactory.initialisationTrial(clientTransfer);

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void prolongSecret(RouterProtos.ClientRequestWithProlongationSecret request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        ClientDtoToRouterWithVpnProfile clientTransfer = new ClientDtoToRouterWithVpnProfile(request);
        String result = vpnManagerFactory.prolongSecret(clientTransfer);
        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}