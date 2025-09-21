# Mi Negocio - Sistema de Gestión de Clientes Full Stack

## Descripción

Sistema completo de gestión de clientes desarrollado con **Spring Boot** (backend) y **React** (frontend) para Alquimiasoft. Permite registrar, editar, eliminar y buscar clientes, así como gestionar sus direcciones (matriz y adicionales). Completamente dockerizado para fácil despliegue.

## Características Principales

### **Backend (Spring Boot)**
- **CRUD completo** para clientes
- **Búsqueda avanzada** por identificación o nombre
- **Gestión de direcciones** (matriz y adicionales)
- **Validaciones de negocio** robustas
- **API REST** documentada y probada
- **Migraciones de BD** con Liquibase
- **Tests completos** (unitarios e integración)
- **Manejo centralizado** de errores

### **Frontend (React)**
- **Interfaz moderna** y responsiva
- **Búsqueda en tiempo real** de clientes
- **Formularios intuitivos** para CRUD
- **Gestión visual** de direcciones
- **Validaciones del lado cliente**
- **Notificaciones** de éxito/error
- **Optimizado** para producción

### **DevOps**
- **Completamente dockerizado**
- **Docker Compose** para orquestación

## Tecnologías Utilizadas

### **Backend**
- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **Spring Validation**
- **PostgreSQL 15**
- **Liquibase**
- **JUnit 5**
- **Mockito**
- **Maven**

### **Frontend**
- **React 18**
- **JavaScript ES6+**
- **CSS3**
- **Fetch API**
- **Responsive Design**

### **Infraestructura**
- **Docker**
- **Docker Compose**
- **PostgreSQL**

## Instalación y Ejecución

### **Prerrequisitos**

- **Docker** 20.10+
- **Docker Compose** v2+
- **Git** (para clonar el repositorio)

### **Opción 1: Con Docker (Recomendado)**

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd minegocio

# 2. Levantar toda la aplicación
docker-compose up --build

# 3. Acceder a la aplicación
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080/api
# PostgreSQL: localhost:5432
```

### **Opción 2: Desarrollo Local**

#### **Backend:**
```bash
# Prerrequisitos: Java 17, Maven, PostgreSQL
cd backend/minegocio

# 1. Configurar base de datos en application.yml
# 2. Ejecutar aplicación
mvn spring-boot:run
```

#### **Frontend:**
```bash
# Prerrequisitos: Node.js 18+
cd frontend

# 1. Instalar dependencias
npm install

# 2. Configurar API URL en .env
echo "REACT_APP_BASE_URL=http://localhost:8080/api" > .env

# 3. Ejecutar en modo desarrollo
npm start
```

## Comandos Docker Útiles

```bash
# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres

# Reconstruir un servicio
docker-compose build backend
docker-compose up -d backend

# Parar todos los servicios
docker-compose down

```

## API REST - Endpoints

### **Base URL:** `http://localhost:8080/api`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/clientes/buscar?empresaId=1&busqueda=Juan` | Buscar clientes |
| `POST` | `/clientes` | Crear cliente con dirección matriz |
| `GET` | `/clientes/{id}` | Obtener cliente por ID |
| `PUT` | `/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/clientes/{id}` | Eliminar cliente |
| `POST` | `/clientes/direcciones` | Crear dirección adicional |
| `GET` | `/clientes/{clienteId}/direcciones` | Listar todas las direcciones |
| `GET` | `/clientes/{clienteId}/direcciones/adicionales` | Listar direcciones adicionales |
| `GET` | `/clientes/{clienteId}/direcciones/matriz` | Obtener dirección matriz |

## Testing

```bash
# Tests unitarios
cd backend/minegocio
mvn test
```

## Documentación Adicional

- **Postman Collection**: `backend/minegocio/docs/MiNegocio API.postman_collection.json`

## Arquitectura y Patrones

### **Principios SOLID**
- **S**: Responsabilidad única por clase
- **O**: Abierto para extensión, cerrado para modificación  
- **L**: Principio de sustitución de Liskov
- **I**: Segregación de interfaces
- **D**: Inversión de dependencias

### **Clean Architecture**
- **Separación clara** de capas (Controller → Service → Repository)
- **DTOs** para transferencia de datos
- **Mappers** para conversión Entity ↔ DTO
- **Exception handling** centralizado
- **Validaciones** en capas apropiadas

## Desarrollado por

- **Proyecto**: Mi Negocio - Sistema de Gestión de Clientes Full Stack
- **Empresa**: Ejercicio planteado por AlquimiaSoft  
- **Stack**: Spring Boot + React + PostgreSQL + Docker
- **Arquitectura**: Clean Architecture + SOLID + TDD
- **DevOps**: Docker + Docker Compose
- **Autor**: Jharol Uchuari

---

**Para ejecutar: `docker-compose up --build`**

## Nota

La Empresa se crea automaticamente en Postgres con **ID**: 1 y **Nombre** Alquimiasoft. Si no se crea por alguna falla en el changelog, agregar un registro en la base de datos.