package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.1)",
    comments = "Source: proto/router.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RouterConnectorGrpc {

  private RouterConnectorGrpc() {}

  public static final String SERVICE_NAME = "proto.RouterConnector";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest,
      proto.RouterProtos.ResponseMessage> getInitialisationSecretMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitialisationSecret",
      requestType = proto.RouterProtos.ClientRequest.class,
      responseType = proto.RouterProtos.ResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest,
      proto.RouterProtos.ResponseMessage> getInitialisationSecretMethod() {
    io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest, proto.RouterProtos.ResponseMessage> getInitialisationSecretMethod;
    if ((getInitialisationSecretMethod = RouterConnectorGrpc.getInitialisationSecretMethod) == null) {
      synchronized (RouterConnectorGrpc.class) {
        if ((getInitialisationSecretMethod = RouterConnectorGrpc.getInitialisationSecretMethod) == null) {
          RouterConnectorGrpc.getInitialisationSecretMethod = getInitialisationSecretMethod =
              io.grpc.MethodDescriptor.<proto.RouterProtos.ClientRequest, proto.RouterProtos.ResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitialisationSecret"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ResponseMessage.getDefaultInstance()))
              .setSchemaDescriptor(new RouterConnectorMethodDescriptorSupplier("InitialisationSecret"))
              .build();
        }
      }
    }
    return getInitialisationSecretMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest,
      proto.RouterProtos.ResponseMessage> getInitialisationTrialMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitialisationTrial",
      requestType = proto.RouterProtos.ClientRequest.class,
      responseType = proto.RouterProtos.ResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest,
      proto.RouterProtos.ResponseMessage> getInitialisationTrialMethod() {
    io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequest, proto.RouterProtos.ResponseMessage> getInitialisationTrialMethod;
    if ((getInitialisationTrialMethod = RouterConnectorGrpc.getInitialisationTrialMethod) == null) {
      synchronized (RouterConnectorGrpc.class) {
        if ((getInitialisationTrialMethod = RouterConnectorGrpc.getInitialisationTrialMethod) == null) {
          RouterConnectorGrpc.getInitialisationTrialMethod = getInitialisationTrialMethod =
              io.grpc.MethodDescriptor.<proto.RouterProtos.ClientRequest, proto.RouterProtos.ResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitialisationTrial"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ResponseMessage.getDefaultInstance()))
              .setSchemaDescriptor(new RouterConnectorMethodDescriptorSupplier("InitialisationTrial"))
              .build();
        }
      }
    }
    return getInitialisationTrialMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret,
      proto.RouterProtos.ResponseMessage> getProlongSecretMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProlongSecret",
      requestType = proto.RouterProtos.ClientRequestWithProlongationSecret.class,
      responseType = proto.RouterProtos.ResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret,
      proto.RouterProtos.ResponseMessage> getProlongSecretMethod() {
    io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret, proto.RouterProtos.ResponseMessage> getProlongSecretMethod;
    if ((getProlongSecretMethod = RouterConnectorGrpc.getProlongSecretMethod) == null) {
      synchronized (RouterConnectorGrpc.class) {
        if ((getProlongSecretMethod = RouterConnectorGrpc.getProlongSecretMethod) == null) {
          RouterConnectorGrpc.getProlongSecretMethod = getProlongSecretMethod =
              io.grpc.MethodDescriptor.<proto.RouterProtos.ClientRequestWithProlongationSecret, proto.RouterProtos.ResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProlongSecret"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ClientRequestWithProlongationSecret.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ResponseMessage.getDefaultInstance()))
              .setSchemaDescriptor(new RouterConnectorMethodDescriptorSupplier("ProlongSecret"))
              .build();
        }
      }
    }
    return getProlongSecretMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret,
      proto.RouterProtos.ResponseMessage> getEstablishSSHMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EstablishSSH",
      requestType = proto.RouterProtos.ClientRequestWithProlongationSecret.class,
      responseType = proto.RouterProtos.ResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret,
      proto.RouterProtos.ResponseMessage> getEstablishSSHMethod() {
    io.grpc.MethodDescriptor<proto.RouterProtos.ClientRequestWithProlongationSecret, proto.RouterProtos.ResponseMessage> getEstablishSSHMethod;
    if ((getEstablishSSHMethod = RouterConnectorGrpc.getEstablishSSHMethod) == null) {
      synchronized (RouterConnectorGrpc.class) {
        if ((getEstablishSSHMethod = RouterConnectorGrpc.getEstablishSSHMethod) == null) {
          RouterConnectorGrpc.getEstablishSSHMethod = getEstablishSSHMethod =
              io.grpc.MethodDescriptor.<proto.RouterProtos.ClientRequestWithProlongationSecret, proto.RouterProtos.ResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EstablishSSH"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ClientRequestWithProlongationSecret.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.RouterProtos.ResponseMessage.getDefaultInstance()))
              .setSchemaDescriptor(new RouterConnectorMethodDescriptorSupplier("EstablishSSH"))
              .build();
        }
      }
    }
    return getEstablishSSHMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RouterConnectorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterConnectorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterConnectorStub>() {
        @java.lang.Override
        public RouterConnectorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterConnectorStub(channel, callOptions);
        }
      };
    return RouterConnectorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RouterConnectorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterConnectorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterConnectorBlockingStub>() {
        @java.lang.Override
        public RouterConnectorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterConnectorBlockingStub(channel, callOptions);
        }
      };
    return RouterConnectorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RouterConnectorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterConnectorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterConnectorFutureStub>() {
        @java.lang.Override
        public RouterConnectorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterConnectorFutureStub(channel, callOptions);
        }
      };
    return RouterConnectorFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RouterConnectorImplBase implements io.grpc.BindableService {

    /**
     */
    public void initialisationSecret(proto.RouterProtos.ClientRequest request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitialisationSecretMethod(), responseObserver);
    }

    /**
     */
    public void initialisationTrial(proto.RouterProtos.ClientRequest request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitialisationTrialMethod(), responseObserver);
    }

    /**
     */
    public void prolongSecret(proto.RouterProtos.ClientRequestWithProlongationSecret request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProlongSecretMethod(), responseObserver);
    }

    /**
     */
    public void establishSSH(proto.RouterProtos.ClientRequestWithProlongationSecret request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEstablishSSHMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInitialisationSecretMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                proto.RouterProtos.ClientRequest,
                proto.RouterProtos.ResponseMessage>(
                  this, METHODID_INITIALISATION_SECRET)))
          .addMethod(
            getInitialisationTrialMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                proto.RouterProtos.ClientRequest,
                proto.RouterProtos.ResponseMessage>(
                  this, METHODID_INITIALISATION_TRIAL)))
          .addMethod(
            getProlongSecretMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                proto.RouterProtos.ClientRequestWithProlongationSecret,
                proto.RouterProtos.ResponseMessage>(
                  this, METHODID_PROLONG_SECRET)))
          .addMethod(
            getEstablishSSHMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                proto.RouterProtos.ClientRequestWithProlongationSecret,
                proto.RouterProtos.ResponseMessage>(
                  this, METHODID_ESTABLISH_SSH)))
          .build();
    }
  }

  /**
   */
  public static final class RouterConnectorStub extends io.grpc.stub.AbstractAsyncStub<RouterConnectorStub> {
    private RouterConnectorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouterConnectorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterConnectorStub(channel, callOptions);
    }

    /**
     */
    public void initialisationSecret(proto.RouterProtos.ClientRequest request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitialisationSecretMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void initialisationTrial(proto.RouterProtos.ClientRequest request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitialisationTrialMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void prolongSecret(proto.RouterProtos.ClientRequestWithProlongationSecret request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProlongSecretMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void establishSSH(proto.RouterProtos.ClientRequestWithProlongationSecret request,
        io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEstablishSSHMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RouterConnectorBlockingStub extends io.grpc.stub.AbstractBlockingStub<RouterConnectorBlockingStub> {
    private RouterConnectorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouterConnectorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterConnectorBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.RouterProtos.ResponseMessage initialisationSecret(proto.RouterProtos.ClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitialisationSecretMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.RouterProtos.ResponseMessage initialisationTrial(proto.RouterProtos.ClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitialisationTrialMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.RouterProtos.ResponseMessage prolongSecret(proto.RouterProtos.ClientRequestWithProlongationSecret request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProlongSecretMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.RouterProtos.ResponseMessage establishSSH(proto.RouterProtos.ClientRequestWithProlongationSecret request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEstablishSSHMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RouterConnectorFutureStub extends io.grpc.stub.AbstractFutureStub<RouterConnectorFutureStub> {
    private RouterConnectorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouterConnectorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterConnectorFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.RouterProtos.ResponseMessage> initialisationSecret(
        proto.RouterProtos.ClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitialisationSecretMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.RouterProtos.ResponseMessage> initialisationTrial(
        proto.RouterProtos.ClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitialisationTrialMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.RouterProtos.ResponseMessage> prolongSecret(
        proto.RouterProtos.ClientRequestWithProlongationSecret request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProlongSecretMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.RouterProtos.ResponseMessage> establishSSH(
        proto.RouterProtos.ClientRequestWithProlongationSecret request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEstablishSSHMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INITIALISATION_SECRET = 0;
  private static final int METHODID_INITIALISATION_TRIAL = 1;
  private static final int METHODID_PROLONG_SECRET = 2;
  private static final int METHODID_ESTABLISH_SSH = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RouterConnectorImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RouterConnectorImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INITIALISATION_SECRET:
          serviceImpl.initialisationSecret((proto.RouterProtos.ClientRequest) request,
              (io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage>) responseObserver);
          break;
        case METHODID_INITIALISATION_TRIAL:
          serviceImpl.initialisationTrial((proto.RouterProtos.ClientRequest) request,
              (io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage>) responseObserver);
          break;
        case METHODID_PROLONG_SECRET:
          serviceImpl.prolongSecret((proto.RouterProtos.ClientRequestWithProlongationSecret) request,
              (io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage>) responseObserver);
          break;
        case METHODID_ESTABLISH_SSH:
          serviceImpl.establishSSH((proto.RouterProtos.ClientRequestWithProlongationSecret) request,
              (io.grpc.stub.StreamObserver<proto.RouterProtos.ResponseMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RouterConnectorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RouterConnectorBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.RouterProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RouterConnector");
    }
  }

  private static final class RouterConnectorFileDescriptorSupplier
      extends RouterConnectorBaseDescriptorSupplier {
    RouterConnectorFileDescriptorSupplier() {}
  }

  private static final class RouterConnectorMethodDescriptorSupplier
      extends RouterConnectorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RouterConnectorMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RouterConnectorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RouterConnectorFileDescriptorSupplier())
              .addMethod(getInitialisationSecretMethod())
              .addMethod(getInitialisationTrialMethod())
              .addMethod(getProlongSecretMethod())
              .addMethod(getEstablishSSHMethod())
              .build();
        }
      }
    }
    return result;
  }
}
