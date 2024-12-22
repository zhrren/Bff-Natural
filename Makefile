SHELL := /bin/bash
HIDE ?= @

export HOMEBREW_NO_AUTO_UPDATE=true

.PHONY: build

name := "Bff-Natural"
port := 12028

clean:
	$(HIDE)./gradlew clean

fix:
	$(HIDE)./gradlew clean
	$(HIDE)./gradlew ktlintFormat

build:
	$(HIDE)./gradlew clean
	$(HIDE)./gradlew installShadowDist

test:
	$(HIDE)./gradlew clean
	$(HIDE)./gradlew test

run:
	$(HIDE)./gradlew run

docker-build:
	$(HIDE)docker build -f Dockerfile -t $(name) .

docker-push: env := "dev"
docker-push: version := $(shell date +%y%m%d%H%M%S)
docker-push: tag := $(name):$(env)-$(version)
docker-push:
	$(HIDE)echo $(tag)
	$(HIDE)docker build -f Dockerfile -t $(tag) .
	$(HIDE)docker tag $(tag) $(ALIYUN_REGISTRY)/weworkci/$(tag)
	$(HIDE)docker push $(ALIYUN_REGISTRY)/weworkci/$(tag)
	$(HIDE)echo $(ALIYUN_REGISTRY)/weworkci/$(tag)

docker-up: image := $(name)
docker-up:
	$(HIDE)docker run -d --name $(name) --env-file .env --publish $(port):$(port) $(image)

docker-down:
	$(HIDE)docker rm -f $(name)

docker-log:
	$(HIDE)docker logs $(name) -f -n 1000
