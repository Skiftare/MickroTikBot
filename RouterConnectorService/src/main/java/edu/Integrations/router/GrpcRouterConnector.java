package edu.Integrations.router;


import edu.Data.dto.ClientTransfer;
import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;
import proto.RouterConnectorGrpc;
import proto.RouterProtos;

import java.math.BigDecimal;
import java.sql.Date;

public class GrpcRouterConnector extends RouterConnectorGrpc.RouterConnectorImplBase implements BindableService {
    private static final String envForProduction = System.getenv("ROUTER_BEHAVIOUR");

    private static ClientTransfer createDtoClassFromRouterDto(RouterProtos.ClientRequest request){
        return new ClientTransfer(
                request.getId(),
                request.getTgUserId(),
                request.getPhone(),
                request.getName(),
                Date.valueOf(request.getUserLastVisited()),
                request.getVpnProfile(),
                request.getIsVpnProfileAlive(),
                Date.valueOf(request.getExpiredAt()),
                request.getIsInPaymentProcess(),
                request.getPaymentKey(),
                new BigDecimal(request.getBalance()),
                new BigDecimal(request.getHeldBalance())
        );
    }

    @Override
    public void initialisationSecret(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {

        ClientTransfer clientTransfer = createDtoClassFromRouterDto(request);
        String result = "";
        if(envForProduction.equals("test")){
            result = TestRouterConnector.initialisationSecret(clientTransfer);
        }
        else{
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
        ClientTransfer clientTransfer = createDtoClassFromRouterDto(request);
        String result = "";
        if(envForProduction.equals("test")){
            result = TestRouterConnector.initialisationTrial(clientTransfer);
        }
        else{
            result = RouterConnector.initialisationTrial(clientTransfer);
        }

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void prolongSecret(RouterProtos.ClientRequest request, StreamObserver<RouterProtos.ResponseMessage> responseObserver) {
        ClientTransfer clientTransfer = createDtoClassFromRouterDto(request);
        String result = "";
        if(envForProduction.equals("test")){
            result = TestRouterConnector.prolongSecret(clientTransfer);
        }
        else{
            result = RouterConnector.prolongSecret(clientTransfer);
        }

        RouterProtos.ResponseMessage response = RouterProtos.ResponseMessage.newBuilder()
                .setMessage(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}