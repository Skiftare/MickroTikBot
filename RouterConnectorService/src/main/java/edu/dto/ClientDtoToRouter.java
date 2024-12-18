package edu.dto;

import jakarta.validation.constraints.NotNull;
import proto.RouterProtos;

public record ClientDtoToRouter
        (@NotNull  Long tgUserId){
    public ClientDtoToRouter(RouterProtos.ClientRequest clientRequest) {
        this(clientRequest.getTgUserId());
    }
}
