-- Crear el usuario
CREATE USER TASKS_USER IDENTIFIED BY password;

GRANT CONNECT, RESOURCE TO TASKS_USER;

ALTER USER TASKS_USER QUOTA UNLIMITED ON USERS;

-- Usar ese esquema
ALTER SESSION SET CURRENT_SCHEMA = TASKS_USER;

-- Crea la tabla de tareas
CREATE TABLE TASKS (
                       TASK_ID NUMBER PRIMARY KEY,
                       TITLE VARCHAR2(100) NOT NULL,
                       DESCRIPTION VARCHAR2(4000),
                       COMPLETED NUMBER(1,0) DEFAULT 0,
                       CREATED_AT TIMESTAMP DEFAULT SYSDATE,
                       UPDATED_AT TIMESTAMP
);
-- Crea la secuencia del id
CREATE SEQUENCE TASK_SEQ START WITH 1 INCREMENT BY 1;

-- Crea el disparador
CREATE OR REPLACE TRIGGER TASK_TRIGGER
BEFORE INSERT OR UPDATE ON TASKS FOR EACH ROW
    BEGIN
        IF INSERTING THEN
            :NEW.TASK_ID := TASK_SEQ.NEXTVAL;
            :NEW.CREATED_AT := SYSDATE;
        END IF;

    :NEW.UPDATED_AT := SYSDATE;
END;
/

-- Crea el paquete
CREATE OR REPLACE PACKAGE TASK_PKG AS
    TYPE task_cursor IS REF CURSOR;

    PROCEDURE GET_ALL_TASKS(p_cursor OUT task_cursor);
    PROCEDURE GET_TASK_BY_ID(p_id IN NUMBER, p_cursor OUT task_cursor);
    PROCEDURE CREATE_TASK(p_title IN VARCHAR2, p_description IN VARCHAR2, p_id OUT NUMBER);
    PROCEDURE UPDATE_TASK(p_id IN NUMBER, p_title IN VARCHAR2, p_description IN VARCHAR2, p_completed IN NUMBER);
    PROCEDURE DELETE_TASK(p_id IN NUMBER);
END TASK_PKG;
/

-- Crea el body
CREATE OR REPLACE PACKAGE BODY TASK_PKG AS

    PROCEDURE GET_ALL_TASKS(p_cursor OUT task_cursor) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM TASKS;
    END;

    PROCEDURE GET_TASK_BY_ID(p_id IN NUMBER, p_cursor OUT task_cursor) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM TASKS WHERE TASK_ID = p_id;
    END;

    PROCEDURE CREATE_TASK(p_title IN VARCHAR2, p_description IN VARCHAR2, p_id OUT NUMBER) IS
    BEGIN
        INSERT INTO TASKS (TITLE, DESCRIPTION) VALUES (p_title, p_description) RETURNING TASK_ID INTO p_id;
    END;

    PROCEDURE UPDATE_TASK(p_id IN NUMBER, p_title IN VARCHAR2, p_description IN VARCHAR2, p_completed IN NUMBER) IS
    BEGIN
        UPDATE TASKS
        SET TITLE = p_title,
        DESCRIPTION = p_description,
        COMPLETED = p_completed
        WHERE TASK_ID = p_id;
    END;

    PROCEDURE DELETE_TASK(p_id IN NUMBER) IS
    BEGIN
        DELETE FROM TASKS WHERE TASK_ID = p_id;
    END;

END TASK_PKG;
/