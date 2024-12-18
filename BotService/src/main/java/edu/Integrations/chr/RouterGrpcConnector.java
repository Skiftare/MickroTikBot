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
    public RouterProtos.ClientRequest reformatDtoForGrpcTransfer(ClientTransfer clientTransfer){
        return RouterProtos.ClientRequest.newBuilder()
                .setId(clientTransfer.id() != null ? clientTransfer.id() : 0L)
                .setTgUserId(clientTransfer.tgUserId() != null ? clientTransfer.tgUserId() : 0L)
                .setPhone(clientTransfer.phone() != null ? clientTransfer.phone() : "")
                .setName(clientTransfer.name() != null ? clientTransfer.name() : "")
                .setUserLastVisited(clientTransfer.userLastVisited() != null
                        ? clientTransfer.userLastVisited().toString()
                        : "")
                .setVpnProfile(clientTransfer.vpnProfile() != null ? clientTransfer.vpnProfile() : "")
                .setIsVpnProfileAlive(clientTransfer.isVpnProfileAlive() != null
                        ? clientTransfer.isVpnProfileAlive()
                        : false)
                .setExpiredAt(clientTransfer.expiredAt() != null
                        ? clientTransfer.expiredAt().toString()
                        : "")
                .setIsInPaymentProcess(clientTransfer.isInPaymentProcess())
                .setPaymentKey(clientTransfer.paymentKey() != null ? clientTransfer.paymentKey() : "")
                .setBalance(clientTransfer.balance() != null ? clientTransfer.balance().toString() : "0")
                .setHeldBalance(clientTransfer.heldBalance() != null ? clientTransfer.heldBalance().toString() : "0")
                .build();
    }

    public RouterGrpcConnector(String target) {
        // Создаём
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target)
                .usePlaintext() // (без TLS)
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
    public String prolongSecret(RouterProtos.ClientRequest request) {
        RouterProtos.ResponseMessage response = stub.prolongSecret(request);
        return response.getMessage();
    }
}
