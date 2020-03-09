# sidecar-security-data

protoc --proto_path=./src/main/protobuf --descriptor_set_out=customer.protoset --include_imports customer.proto
grpcurl -protoset ./customer.protoset list 
grpcurl -protoset ./customer.protoset list Customer
grpcurl -protoset ./customer.protoset describe Customer.getCustomer
grpcurl -emit-defaults -protoset ./customer.protoset describe Customer.getCustomer
grpcurl -plaintext -protoset ./customer.protoset -d '{"version" : "1","accountNumber" : "12334"}' localhost:50051 Customer.getCustomer

grpcurl -plaintext -d '{"version" : "1","accountNumber" : "12334"}' localhost:50051 Customer.getCustomer