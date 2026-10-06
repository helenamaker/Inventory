FROM maven:3.9.16-eclipse-temurin-8 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:8.5.87-jdk8-temurin-focal

RUN rm -rf /usr/local/tomcat/webapps/ROOT
RUN mkdir -p /app/data

ENV INVENTORY_DATA_PATH=/app/data
ENV UPLOADS_PATH=/app/data

COPY --from=build /app/target/Inventory.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
