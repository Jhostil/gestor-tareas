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

        List<Task> tasks = new ArrayList<>();

        String sql = "{ call TASK_PKG.GET_ALL_TASKS(?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setTaskId(rs.getLong("TASK_ID"));
                    task.setTitle(rs.getString("TITLE"));
                    task.setDescription(rs.getString("DESCRIPTION"));
                    task.setCompleted(rs.getInt("COMPLETED"));

                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * Método para crear una tarea
     * @param task Tarea a guardar
     */
    public Task createTask(Task task) {

        String sql = "INSERT INTO TASKS (TITLE, DESCRIPTION, COMPLETED) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getCompleted());

            ps.executeUpdate();

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT TASK_SEQ.CURRVAL FROM dual")) {

                if (rs.next()) {
                    task.setTaskId(rs.getLong(1));
                }
            }
            return task;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}