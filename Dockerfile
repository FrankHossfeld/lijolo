FROM maven:3.9.10-eclipse-temurin-21-alpine

# TODO change the ip to the one of the local computer!
ENV host=192.168.178.28
ENV port=5439
ENV password=postgres
ENV user=postgres
ENV database=lijolo-prod
ENV contexts=prod_update

# set the working directory in the container
WORKDIR /updater

# Copy the JAR file into the contiaenr at /updater
COPY target/lijolo-model-liquibase-updater.jar /updater/lijolo-model-liquibase-updater.jar

CMD ["sh", "-c", "java \
-Dhost=$host \
-Dport=$port \
-Duser=$user \
-Dpassword=$password \
-Ddatabase=$database \
-Dcontexts=$contexts \
-jar lijolo-model-liquibase-updater.jar"]


