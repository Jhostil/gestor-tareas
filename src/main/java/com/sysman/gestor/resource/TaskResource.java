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

    /**
     * Endpoint para obtener una tarea dado su ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") Long id) {
        try {
            Task task = taskDAO.getTaskById(id);

            if (task != null) {
                return Response.ok(task).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No existe una tarea con el id: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las tareas: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Endpoint para crear una tarea
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(Task task) {
        try {
            Task taskCreated = taskDAO.createTask(task);
            return Response.status(Response.Status.CREATED)
                    .entity(taskCreated)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la tarea: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Endpoint para actualizar una tarea
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") Long id, Task task) {

        Task existing = taskDAO.getTaskById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tarea no encontrada")
                    .build();
        }
        try {
            task.setTaskId(id);
            taskDAO.updateTask(task);
            return Response.ok(task).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar la tarea: " + e.getMessage())
                    .build();
        }
    }
}