package com.sysman.gestor.dao;

import com.sysman.gestor.model.Task;
import com.sysman.gestor.util.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
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
}