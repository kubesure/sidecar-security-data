package io.kubesure.sidecar.security.data;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.services.HealthStatusManager;
import io.grpc.stub.StreamObserver;
import io.kubesure.sidecar.security.data.CustomerGrpc.CustomerImplBase;
import io.kubesure.sidecar.security.data.SidecarProtos.CustomerRequest;
import io.kubesure.sidecar.security.data.SidecarProtos.CustomerResponse;
import io.kubesure.sidecar.security.data.SidecarProtos.CustomerResponse.Builder;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    private Server server;

    public static void main(final String[] args) throws IOException, InterruptedException {
        final App server = new App();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        final int port = 50051;
        final ServerBuilder sBuilder = ServerBuilder.forPort(port);
        sBuilder.addService(new CustomerImpl());
        sBuilder.addService(new HealthStatusManager().getHealthService());
        sBuilder.addService(ProtoReflectionService.newInstance());
        server = sBuilder.build();
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("*** shutting down gRPC server since JVM is shutting down");
                App.this.stop();
                logger.info("*** server shut down");
            }
        });
    }

    private class CustomerImpl extends CustomerImplBase {
        @Override
        public void getCustomer(CustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
            //super.getCustomer(request, responseObserver);
            logger.info("get Customer info");
            logger.info("account Number " + request.getAccountNumber());
            try {
                //Customer customer = IgniteHelper.getCustomerByID(request.getAccountNumber());
                Builder builder = CustomerResponse.newBuilder();
                long cif = 12333l;
                builder.setCIF(cif);
                builder.setOk(true);
                CustomerResponse response = builder.build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("error occured" + e.getMessage());
                logger.severe(e.getMessage());
                responseObserver.onCompleted();    
            }
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
