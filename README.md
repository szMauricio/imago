# 🛍️ Imago Store - Backend API

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![JWT](https://img.shields.io/badge/JWT-Auth-yellow)

**API REST completa para tienda online de ropa desarrollada con Spring Boot**

[Características](#-características) • [Tecnologías](#-tecnologías) • [Instalación](#-instalación) • [API Docs](#-documentación-de-la-api) • [Estructura](#-estructura-del-proyecto)

</div>

## 📋 Tabla de Contenidos

- [🎯 Características](#-características)
- [🛠️ Tecnologías](#️-tecnologías)
- [🚀 Instalación](#-instalación)
- [📚 Documentación de la API](#-documentación-de-la-api)
- [🏗️ Estructura del Proyecto](#️-estructura-del-proyecto)
- [🔐 Autenticación](#-autenticación)
- [📊 Endpoints Principales](#-endpoints-principales)
- [🧪 Testing](#-testing)
- [🔧 Configuración](#-configuración)
- [🤝 Contribución](#-contribución)

## 🎯 Características

### **Funcionalidades Principales**
- 🔐 **Autenticación JWT** con roles de usuario (CLIENTE, ADMIN)
- 👥 **Gestión de usuarios** (registro, login, CRUD)
- 🛍️ **Catálogo de productos** con sistema de categorías
- 🛒 **Carrito de compras** completo con gestión de items
- 📦 **Sistema de órdenes** con estados (PENDING, CONFIRMED, SHIPPED, etc.)
- 📱 **API RESTful** completamente documentada
- 🛡️ **Seguridad robusta** con Spring Security

### **Características Técnicas**
- ✅ **Arquitectura en capas** (Controller → Service → Repository)
- ✅ **Validación de datos** con Bean Validation
- ✅ **Manejo centralizado de excepciones**
- ✅ **DTOs** para transferencia de datos segura
- ✅ **Configuración por environments** (dev, prod)
- ✅ **Documentación automática** con OpenAPI/Swagger

## 🛠️ Tecnologías

### **Backend**
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.7** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **JWT** - JSON Web Tokens para autenticación

### **Base de Datos**
- **PostgreSQL** - Base de datos relacional
- **Hibernate** - ORM

### **Herramientas de Desarrollo**
- **Maven** - Gestión de dependencias
- **SpringDoc OpenAPI** - Documentación automática

## 🚀 Instalación

### **Prerrequisitos**
- Java 21 o superior
- Maven 3.6+
- PostgreSQL 12+
- Git

### **1. Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/imago.git
cd imago
```

### **2. Configurar base de datos**
```sql
CREATE DATABASE imago_tienda;
```

### **3. Configurar variables de entorno**
Crea `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/imago_tienda
spring.datasource.username=postgres
spring.datasource.password=tu_password

jwt.secret=tu_jwt_secret_super_seguro_aqui
jwt.expiration=86400000

server.port=8080
server.servlet.context-path=/api
```

### **4. Ejecutar la aplicación**
```bash
./mvnw clean compile
./mvnw spring-boot:run

# O compilar JAR
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 📚 Documentación de la API

**Swagger UI:**  
http://localhost:8080/api/swagger-ui.html

**OpenAPI JSON:**  
http://localhost:8080/api/api-docs

---

## 🏗️ Estructura del Proyecto
```text
src/main/java/com/imago/backend/
├── config/
├── controllers/
├── services/
├── repositories/
├── models/
├── dto/
├── exceptions/
├── filters/
└── handlers/
```

## 🔐 Autenticación

**Flujo JWT**
1. Registro/Login → Obtener token JWT  
2. Requests protegidas → Header `Authorization: Bearer <token>`

**Roles**
- `ROLE_CLIENTE`
- `ROLE_ADMIN`

---

## 📊 Endpoints Principales

| Método | Endpoint | Descripción | Auth | Rol |
|--------|-----------|-------------|------|-----|
| POST | /users/register | Registrar usuario | ❌ | - |
| POST | /users/login | Login y obtener JWT | ❌ | - |
| GET | /users | Listar usuarios | ✅ | ADMIN |
| GET | /products | Listar productos | ❌ | - |
| POST | /cart/items | Agregar al carrito | ✅ | CLIENTE |
| POST | /orders | Crear orden | ✅ | CLIENTE |

---

## 🔧 Configuración

**Perfiles de Spring**
- dev
- prod

**Variables de entorno (producción):**
```bash
export DB_URL=jdbc:postgresql://servidor:5432/imago_tienda
export DB_USERNAME=usuario_prod
export DB_PASSWORD=password_seguro
export JWT_SECRET=super_secret_jwt_key_production
export SPRING_PROFILES_ACTIVE=prod
```

## 👨‍💻 Autor

**szMauricio**  
GitHub: [@SzMauricio](https://github.com/szMauricio)  

---

</div>
