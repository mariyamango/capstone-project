# Specifying the base image
FROM --platform=linux/amd64 openjdk:21
EXPOSE 8080
# Copy jar into image
ADD backend/target/carhealth-monitor.jar carhealth-monitor.jar
# What command should be run when the container is first launched?
# java -jar app.jar
ENTRYPOINT ["java", "-jar", "carhealth-monitor.jar"]