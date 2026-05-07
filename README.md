# Task Manager API

REST API para gestión de tareas, construida con Java + JAX-RS y Oracle XE vía Docker. La lógica de base de datos está encapsulada en un paquete PL/SQL (`TASK_PKG`).

---

## Stack

- Java 17 / JAX-RS (Jersey)
- JDBC puro
- Oracle XE 21c (Docker)
- PL/SQL — Packages, Triggers, Sequences
- Apache Tomcat 9
- Maven 3.8

---

## Arquitectura

```
Cliente (Postman / Frontend)
        ↓
REST API — JAX-RS (Jersey)
        ↓
DAO — JDBC
        ↓
Oracle DB — TASK_PKG (PL/SQL)
```

---

## Base de datos

**Tabla `TASKS`**

| Columna | Tipo | Notas |
|---|---|---|
| TASK_ID | NUMBER | PK, generado por secuencia + trigger |
| TITLE | VARCHAR2 | |
| DESCRIPTION | VARCHAR2 | |
| COMPLETED | NUMBER(1) | 0 = pendiente, 1 = completada |
| CREATED_AT | TIMESTAMP | trigger en INSERT |
| UPDATED_AT | TIMESTAMP | trigger en UPDATE |

**Paquete `TASK_PKG`**

Contiene los procedimientos: `GET_ALL_TASKS`, `GET_TASK_BY_ID`, `CREATE_TASK`, `UPDATE_TASK`, `DELETE_TASK`.

**Datos de conexión**

| Campo | Valor |
|---|---|
| Host | localhost |
| Puerto | 1521 |
| Usuario | TASKS_USER |
| Password | password |
| Service | XEPDB1 |
 
---

---
# Configuración y ejecución del proyecto

## Clonar el repositorio

```bash
git clone https://github.com/Jhostil/gestor-tareas.git
```
## Navegar a la carpeta del proyecto

```bash
cd gestor-tareas
```

## Levantar la base de datos

**Requisitos:** Docker y Docker Compose instalados.

```bash
docker-compose up -d
```

## Ejecutar el backend


## Despliegue

### Opción 1 — Apache Tomcat (manual)

**1. Generar el WAR**

```bash
mvn clean package
```

El archivo queda en `target/gestor-tareas.war`.

**2. Copiar el WAR a Tomcat**

Linux/Mac:
```bash
cp target/gestor-tareas.war /opt/tomcat/webapps/
```

Windows: copiar manualmente el `.war` a `TOMCAT_HOME\webapps\`.

**3. Iniciar Tomcat**

Linux/Mac:
```bash
sh /opt/tomcat/bin/startup.sh
```

Windows:
```bat
startup.bat
```
 
---

### Opción 2 — IntelliJ IDEA

1. Ir a **Run → Edit Configurations**
2. Agregar nueva configuración: **Tomcat Server → Local**
3. En la pestaña **Deployment**, presionar `+` y seleccionar `Artifact → gestor-tareas:war exploded`
4. Definir el **Application Context**, por ejemplo: `/gestor-tareas`
5. Ejecutar con el botón Run. La URL resultante sería:

```
http://localhost:8080/gestor-tareas/api/tasks
```
 
---

## API Reference

Base URL: `http://localhost:8080/gestor-tareas/api/tasks`
## EndPoints

**GET** `/api/tasks` — Obtener todas las tareas

**GET** `/api/tasks/{id}` — Obtener tarea por ID

**POST** `/api/tasks` — Crear tarea

```json
{
  "title": "Tarea 1",
  "description": "Descripción de la tarea"
}
```

**PUT** `/api/tasks/{id}` — Actualizar tarea

```json
{
  "title": "Tarea 1",
  "description": "Nueva descripción",
  "completed": 1
}
```

**DELETE** `/api/tasks/{id}` — Eliminar tarea

---

## Notas

- `completed` se maneja como `0/1` (no booleano)
- `CREATED_AT` y `UPDATED_AT` son gestionados por triggers en Oracle
- El `TASK_ID` se genera automáticamente mediante una secuencia
- La serialización JSON la realiza JAX-RS
- CORS habilitado para desarrollo local
- Los scripts SQL de creación de tablas, secuencias, triggers y paquetes se ejecutan automáticamente al iniciar el contenedor Oracle.