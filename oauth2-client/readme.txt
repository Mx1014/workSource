
build:
mvn clean install
run:
cd target
java -jar oauth2-client-0.0.1-SNAPSHOT.war --spring.config.name=oauth2-client

