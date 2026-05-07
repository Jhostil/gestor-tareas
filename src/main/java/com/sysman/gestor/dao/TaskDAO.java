package com.sysman.gestor.dao;

import com.sysman.gestor.model.Task;
import com.sysman.gestor.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO que se encarga de la gestión de tareas
 */
public class TaskDAO {

    /**
     * Obtiene todas las tareas registradas en la base de datos
     * @return Retorna la lista de tareas
     */
    public List<Task> getAllTasks() {

        List<Task> list = new ArrayList<>();

        String sql = "{ call TASK_PKG.GET_ALL_TASKS(?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                while (rs.next()) {
                    Task task = new Task();
                    task.setTaskId(rs.getLong("TASK_ID"));
                    task.setTitle(rs.getString("TITLE"));
                    task.setDescription(rs.getString("DESCRIPTION"));
                    task.setCompleted(rs.getInt("COMPLETED"));
                    task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    task.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));

                    list.add(task);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las tareas " + e.getMessage(), e);
        }

        return list;
    }

    /**
     * Obtiene una tarea dado su identificador
     * @return Retorna la tarea si existe
     */
    public Task getTaskById(Long id) {

        String sql = "{ call TASK_PKG.GET_TASK_BY_ID(?, ?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);
            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                if (rs.next()) {
                    Task task = new Task();
                    task.setTaskId(rs.getLong("TASK_ID"));
                    task.setTitle(rs.getString("TITLE"));
                    task.setDescription(rs.getString("DESCRIPTION"));
                    task.setCompleted(rs.getInt("COMPLETED"));
                    task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    task.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
                    return task;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la tarea "+ e.getMessage(), e);
        }

        return null;
    }

    /**
     * Método para crear una tarea
     * @param task Tarea a guardar
     * @return Retorna la tarea que se creó
     */
    public Task createTask(Task task) {

        String sql = "{ call TASK_PKG.CREATE_TASK(?, ?, ?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, task.getTitle());
            cs.setString(2, task.getDescription());

            cs.registerOutParameter(3, java.sql.Types.NUMERIC);

            cs.execute();

            long generatedId = cs.getLong(3);
            return getTaskById(generatedId);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarea " + e.getMessage(), e);
        }
    }

    /**
     * Método que permite actualizar una tarea
     * @param task Tarea a actualizar
     */
    public void updateTask(Task task) {

        String sql = "{ call TASK_PKG.UPDATE_TASK(?, ?, ?, ?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, task.getTaskId());
            cs.setString(2, task.getTitle());
            cs.setString(3, task.getDescription());
            cs.setInt(4, task.getCompleted());

            cs.execute();

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la tarea " + e.getMessage(), e);
        }
    }

    /**
     * Método que permite eliminar una tarea
     * @param id Identificador de la tarea a eliminar
     */
    public void deleteTask(Long id) {

        String sql = "{ call TASK_PKG.DELETE_TASK(?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);

            cs.execute();

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la tarea " + e.getMessage(), e);
        }
    }
}