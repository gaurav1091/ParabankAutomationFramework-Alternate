FROM maven:3.9.9-eclipse-temurin-11

WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
COPY testng.xml .
COPY README.md .
COPY .mvn ./.mvn
COPY src ./src
COPY docker/entrypoint.sh /usr/local/bin/docker-entrypoint.sh

RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]