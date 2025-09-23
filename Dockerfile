# Java 17
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# backend/mvnw, backend/.mvn, backend/src, frontend/dist
COPY backend/mvnw backend/mvnw.cmd backend/pom.xml ./
COPY backend/.mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY frontend/dist src/main/resources/static
COPY backend/src src
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/almacen-0.0.1-SNAPSHOT.jar"]


