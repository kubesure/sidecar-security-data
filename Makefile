GRADLE=gradle
DOCKER=docker
DBUILD=$(DOCKER) build
DTAG= $(DOCKER) tag
DPUSH= $(DOCKER) push

BINARY_NAME=sidecar-security-data
BINARY_VERSION=$(shell git rev-parse HEAD)
TAG_LOCAL = $(BINARY_NAME):$(BINARY_VERSION)
TAG_HUB = bikertales/$(BINARY_NAME):$(BINARY_VERSION)

.PHONY: pull # - Pull latest from master
pull:
	git pull

.PHONY: build # - Run gradle build
build:
	gradle clean install -x test
	go get github.com/grpc-ecosystem/grpc-health-probe
	go build -tags netgo -ldflags=-w github.com/grpc-ecosystem/grpc-health-probe

.PHONY: run # - Runs the service without build
run: 
	./build/install/sidecar-security-data/bin/sidecar-security-data

.PHONY: dbuild  # - Builds docker image
dbuild: build

	$(DBUILD) . -t $(TAG_LOCAL)

.PHONY: dtag # - Tags local image to docker hub tag
dtag: dbuild
	$(DTAG) $(TAG_LOCAL) $(TAG_HUB)

.PHONY: dpush # - Pushes tag to docker hub
dpush: dtag
	$(DPUSH) $(TAG_HUB)

.PHONY: tasks
tasks:
	@grep '^.PHONY: .* #' Makefile | sed 's/\.PHONY: \(.*\) # \(.*\)/\1 \2/' | expand -t20