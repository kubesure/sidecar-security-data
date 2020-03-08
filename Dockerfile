FROM openjdk:12-jdk-alpine
WORKDIR /app
COPY build/install/sidecar-security-data/lib /app/lib
COPY build/install/sidecar-security-data/bin /app/bin 
COPY grpc-health-probe /app/bin
EXPOSE 50051
ENTRYPOINT ["/app/bin/sidecar-security-data"]