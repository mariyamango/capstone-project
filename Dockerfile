FROM --platform=linux/amd64 openjdk:21

WORKDIR /app

COPY backend/pom.xml .
COPY backend/mvnw .
COPY backend/.mvn .mvn

RUN chmod +x mvnw

RUN ./mvnw dependency:resolve

COPY backend/. .
RUN chmod +x mvnw && ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/carhealth-monitor.jar"]
