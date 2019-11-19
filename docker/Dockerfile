FROM maven:latest AS build-env

ARG ROSAENLG_VERSION

WORKDIR /app

RUN mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -Dartifact=org.rosaenlg:java-server:${ROSAENLG_VERSION}:war
RUN mvn dependency:copy -Dartifact=org.rosaenlg:java-server:${ROSAENLG_VERSION}:war -DoutputDirectory=/app
RUN mv /app/java-server-${ROSAENLG_VERSION}.war /app/java-server.war

# RUN wget https://oss.sonatype.org/content/repositories/snapshots/org/rosaenlg/java-server/1.5.0-SNAPSHOT/java-server-1.5.0-20191104.123523-1.war
# RUN mv java-server-1.5.0-20191104.123523-1.war /app/java-server-1.5.0-SNAPSHOT.war


FROM gcr.io/distroless/java:8

COPY --from=build-env /app /app
WORKDIR /app

CMD ["/app/java-server.war"]
