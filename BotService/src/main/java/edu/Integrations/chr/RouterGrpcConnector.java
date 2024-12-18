package edu.Integrations.chr;

import edu.Data.dto.ClientTransfer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.logging.Log;
import proto.RouterConnectorGrpc;
import proto.RouterProtos;

import java.util.logging.Logger;

public class RouterGrpcConnector {
    private final RouterConnectorGrpc.RouterConnectorBlockingStub stub;
    public RouterProtos.ClientRequestWithProlongationSecret reformatToProlongation(ClientTransfer clientTransfer){
        return RouterProtos.ClientRequestWithProlongationSecret.newBuilder()
                .setTgUserId(clientTransfer.tgUserId())
                .setProlongationSecret(clientTransfer.vpnProfile())
                .build();
    }
    public RouterProtos.ClientRequest reformatToInitialization(ClientTransfer clientTransfer){
        return RouterProtos.ClientRequest.newBuilder()
                .setTgUserId(clientTransfer.tgUserId())
                .build();
    }
    public RouterGrpcConnector(String name, int port) {
        // Создаём
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(name, port)
                .usePlaintext() // (без TLS)
                .build();

        this.stub = RouterConnectorGrpc.newBlockingStub(channel);
    }
    public RouterGrpcConnector(String target){
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target)
                .usePlaintext()
                .build();
        this.stub = RouterConnectorGrpc.newBlockingStub(channel);
    }

    // Метод для отправки ClientRequest и получения ответа

    public String initialisationSecret(RouterProtos.ClientRequest request) {
        Logger.getAnonymousLogger().info("Doing response");
        RouterProtos.ResponseMessage response = stub.initialisationSecret(request);
        return response.getMessage();
    }
    public String initialisationTrial(RouterProtos.ClientRequest request) {
        RouterProtos.ResponseMessage response = stub.initialisationTrial(request);
        return response.getMessage();
    }
    public String prolongSecret(RouterProtos.ClientRequestWithProlongationSecret request) {
        RouterProtos.ResponseMessage response = stub.prolongSecret(request);
        return response.getMessage();
    }
}
