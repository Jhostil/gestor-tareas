# Task Manager API

REST API para gestión de tareas, construida con Java + JAX-RS y Oracle XE vía Docker. La lógica de base de datos está encapsulada en un paquete PL/SQL (`TASK_PKG`).

---

## Stack

- Java 17 / JAX-RS (Jersey)
- JDBC puro
- Oracle XE 21c (Docker)
- PL/SQL — Packages, Triggers, Sequences
- Apache Tomcat
- Maven

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

```bash
mvn clean install
```

Copiar el `.war` generado en `tomcat/webapps/`, o ejecutar directamente desde el IDE.

---

## API Reference

La URL base depende de:

- El puerto configurado localmente en Apache Tomcat
- El context path configurado localmente para la aplicación

Ejemplo de entorno local:

`
http://localhost:8080/gestor-tareas/api/tasks
`

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