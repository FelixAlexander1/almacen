# 1️⃣ Usamos Java 17 JDK
FROM eclipse-temurin:17-jdk-alpine

# 2️⃣ Definimos carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3️⃣ Copiamos mvnw y archivos de Maven
COPY mvnw . 
COPY mvnw.cmd . 
COPY pom.xml .
COPY .mvn .mvn

# 4️⃣ Instalamos dependencias sin compilar para usar cache de Docker
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# 5️⃣ Copiamos el resto del proyecto
COPY src src

# 6️⃣ Construimos la aplicación
RUN ./mvnw clean package -DskipTests

# 7️⃣ Exponemos el puerto en el que corre Spring Boot
EXPOSE 8080

# 8️⃣ Comando para arrancar la app
CMD ["java", "-jar", "target/almacen-0.0.1-SNAPSHOT.jar"]
