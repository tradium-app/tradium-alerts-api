#
# Build stage
#
FROM maven:3.8.3-openjdk-11-slim AS build
WORKDIR /home/app
COPY src src
COPY .mvn .mvn
COPY mvnw pom.xml local-settings.xml ./
RUN mvn -f pom.xml clean package -DskipTests --quiet

#
# Package stage
#
FROM openjdk:11.0.12

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get -y install sudo && \
    apt-get install python3 && \
    apt install -y python3-pip

WORKDIR /home/app

COPY --from=build /home/app/target/swing-trade-alerts-0.0.1-SNAPSHOT.jar ./swing-trade-alerts.jar
COPY data-mining ./data-mining

RUN pip install -r ./data-mining/requirements.txt
EXPOSE 8080

CMD java -Dserver.port=$PORT -jar "/usr/local/lib/swing-trade-alerts.jar"
