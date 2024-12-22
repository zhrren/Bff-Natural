FROM registry.cn-zhangjiakou.aliyuncs.com/publicci/openjdk:17-jdk-alpine-make

COPY . /workspace/src

WORKDIR /workspace/src
RUN ./gradlew installShadowDist

FROM registry.cn-zhangjiakou.aliyuncs.com/publicci/openjdk:17-jdk-alpine-make


COPY --from=0 /workspace/src/build/install/Bff-Natural-shadow /workspace/apps

WORKDIR /workspace/apps
EXPOSE 12028
CMD ./bin/Bff-Natural
