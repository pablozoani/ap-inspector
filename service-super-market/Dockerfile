# Base image -> https://hub.docker.com/layers/openjdk/library/openjdk/11.0.9-jre/images/sha256-7d52f205ad28360c4efe3c9b405e1c1a3a99c358994fd9e8d9d8aca1e869d7ae?context=explore
FROM openjdk:11.0.9-jre

# Add the generated artifact to this docker image
COPY ./target/*.jar /service-super-market.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker,handlers", "/service-super-market.jar"]
