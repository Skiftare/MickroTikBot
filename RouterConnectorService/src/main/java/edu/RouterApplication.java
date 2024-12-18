package edu;

import edu.Integrations.connector.GrpcRouterConnector;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class RouterApplication {
    public static void main(String[] args) throws IOException {
        int port = 8090;
        Logger.getAnonymousLogger().info("Starting");
        Server server = ServerBuilder
                .forPort(port)
                .addService(new GrpcRouterConnector())
                .build()
                .start();
        while (true) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}
