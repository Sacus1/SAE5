FROM sapmachine:21-jdk-ubuntu
RUN apt-get update && apt-get install -y maven

# copy the source tree and the pom.xml to our new container
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

# change working directory to project
WORKDIR /usr/src/app

# package our application code
RUN mvn package

# setup environment
ENV DATABASE_PASSWORD=password
# set the startup command to execute the jar
CMD ["java", "-jar","target/SAE-1.0-SNAPSHOT-jar-with-dependencies.jar"]
