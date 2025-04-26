FROM openjdk:23-ea-17-jdk
WORKDIR /app
COPY ./target/products-microservice-0.0.1-SNAPSHOT.jar .
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "products-microservice-0.0.1-SNAPSHOT.jar"]
