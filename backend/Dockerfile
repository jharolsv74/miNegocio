# Dockerfile para Spring Boot Backend
FROM openjdk:17-jdk-slim

# Información del maintainer
LABEL maintainer="Jharol Uchuari"
LABEL description="Mi Negocio - Spring Boot Backend API"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven
COPY minegocio/pom.xml ./
COPY minegocio/mvnw ./
COPY minegocio/mvnw.cmd ./
COPY minegocio/.mvn ./.mvn

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Descargar dependencias (esto se cachea si no cambia pom.xml)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY minegocio/src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer el puerto 8080
EXPOSE 8080

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/minegocio-0.0.1-SNAPSHOT.jar"]
