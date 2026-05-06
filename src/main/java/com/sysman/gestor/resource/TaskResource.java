package com.sysman.gestor.resource;

import com.sysman.gestor.dao.TaskDAO;
import com.sysman.gestor.model.Task;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
public class TaskResource {

    private TaskDAO taskDAO = new TaskDAO();

    /**
     * Endpoint para obtener todas las tareas
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasks() {
        try {
            List<Task> tasks = taskDAO.getAllTasks();
            return Response.ok(tasks).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las tareas: " + e.getMessage())
                    .build();
        }
    }
    
}