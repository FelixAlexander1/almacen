# Imagen base para Java
FROM eclipse-temurin:17-jdk-alpine AS backend-build
WORKDIR /app

# Copiamos mvnw y Maven Wrapper
COPY backend/mvnw backend/mvnw.cmd backend/pom.xml ./
COPY backend/.mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiamos el backend
COPY backend/src src
RUN ./mvnw clean package -DskipTests

# Imagen base para Node y construir frontend
FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Imagen final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos backend construido
COPY --from=backend-build /app/target/almacen-0.0.1-SNAPSHOT.jar ./target/

# Copiamos frontend construido
COPY --from=frontend-build /frontend/dist ./src/main/resources/static

EXPOSE 8080
CMD ["java", "-jar", "target/almacen-0.0.1-SNAPSHOT.jar"]
