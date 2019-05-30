FROM openjdk:8-jre-alpine

ARG JAR_PRODUCT_NAME
ARG JAR_PRODUCT_VERSION
ARG PRODUCT_INTERNAL_PORT

WORKDIR /opt/com.mavenir/demo-echo/echobe

COPY ./build/libs/$JAR_PRODUCT_NAME-$JAR_PRODUCT_VERSION.jar ./

RUN addgroup echogroup && \
	adduser -D -H echouser -G echogroup && \
	ln -s ./$JAR_PRODUCT_NAME-$JAR_PRODUCT_VERSION.jar ./echobe.jar

USER echouser

ENTRYPOINT ["java","-jar","./echobe.jar"]

EXPOSE $PRODUCT_INTERNAL_PORT

LABEL maintainer="tiran.meltser@efrat.com"
