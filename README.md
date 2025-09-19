# 📊 Mi Negocio - Gestión de Clientes

Este proyecto fue desarrollado como parte de la prueba técnica para **Alquimiasoft**.  
El objetivo es implementar un servicio backend en **Java (Spring Boot + PostgreSQL)** que gestione clientes y sus direcciones (incluyendo dirección matriz).

---

## Tecnologías
- Java 11
- Spring Boot 2.7.x
- PostgreSQL
- JPA / Hibernate
- JUnit 5 + Mockito
- Maven
- Liquibase (opcional para versionamiento de DB)

---

## Requisitos
- Java 11+
- PostgreSQL 13+
- Maven 3.6+

---

## Ejecución
1. Configurar credenciales de la BD en `src/main/resources/application.properties`
2. Crear la base de datos:
   ```sql
   CREATE DATABASE mi_negocio_db;
