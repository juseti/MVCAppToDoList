package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.TaskRepository;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    @InjectMocks
    private TaskServiceImpl taskServiceMock;



    @Test
    public void createTaskTest() {
        Task task = new Task();
        task.setName("new Task");

        when(taskRepositoryMock.save(task)).thenReturn(task);

        Task actual = taskServiceMock.create(task);

        verify(taskRepositoryMock).save(task);
        assertEquals(task, actual);
    }


    @Test
    public void readByIdTaskTest() {
        long id = 1;
        Task task = new Task();
        task.setId(id);
        task.setName("New Task");

        when(taskRepositoryMock.findById(task.getId())).thenReturn(Optional.of(task));

        Task actual = taskServiceMock.readById(id);

        verify(taskRepositoryMock).findById(task.getId());
        assertEquals(task, actual);
    }


    @Test
    public void readByNotExistingIdTaskTest() {
        long notExistingId = 2;

        when(taskRepositoryMock.findById(notExistingId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> taskServiceMock.readById(notExistingId)
        );

        verify(taskRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Task with id " + notExistingId + " not found");
    }


    @Test
    public void updateTaskTest() {
        long id = 1;
        Task task = new Task();
        task.setId(id);
        task.setName("New Task");

        when(taskRepositoryMock.findById(id)).thenReturn(Optional.of(task));

        task.setName("New Task #1");

        when(taskRepositoryMock.save(task)).thenReturn(task);

        Task actual = taskServiceMock.update(task);

        verify(taskRepositoryMock).findById(id);
        verify(taskRepositoryMock).save(task);
        assertEquals(task, actual);
    }


    @Test
    public void updateNullTaskTest() {
        NullEntityReferenceException thrown = assertThrows(
                NullEntityReferenceException.class,
                () -> taskServiceMock.update(null)
        );

        assertEquals(NullEntityReferenceException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Task cannot be 'null'");
    }



    @Test
    public void updateNotExistingTaskTest() {
        long notExistingId = 1;
        Task task = new Task();
        task.setId(notExistingId);
        task.setName("New Task #1");

        when(taskRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("Task with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> taskServiceMock.update(task)
        );

        verify(taskRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Task with id " + notExistingId + " not found");
    }


    @Test
    public void deleteTaskTest() {
        long id = 1;
        Task task = new Task();
        task.setId(id);
        task.setName("New Task");

        when(taskRepositoryMock.findById(id)).thenReturn(Optional.of(task));

        doNothing().when(taskRepositoryMock).delete(task);

        taskServiceMock.delete(id);

        verify(taskRepositoryMock).findById(id);
        verify(taskRepositoryMock).delete(task);
        assertFalse(taskServiceMock.getAll().contains(task));
    }


    @Test
    public void deleteNotExistingTaskTest() {
        long notExistingId = 1;

        when(taskRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("Task with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> taskServiceMock.delete(notExistingId)
        );

        verify(taskRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Task with id " + notExistingId + " not found");
    }


    @Test
    public void deleteNullTaskTest() {
        long id = 1;

        when(taskRepositoryMock.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> taskServiceMock.delete(id)
        );

        verify(taskRepositoryMock).findById(id);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Task with id " + id + " not found");
    }



    @Test
    public void getAllTaskTest() {
        long id = 1;
        Task task = new Task();
        task.setId(id);
        task.setName("New Task");

        when(taskRepositoryMock.findAll()).thenReturn(Collections.singletonList(task));

        List<Task> actual = taskServiceMock.getAll();

        verify(taskRepositoryMock).findAll();
        assertEquals(1, actual.size());
    }


    @Test
    public void getAllEmptyListTaskTest() {
        when(taskRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        List<Task> actual = taskServiceMock.getAll();

        verify(taskRepositoryMock).findAll();
        assertTrue(actual.isEmpty());
    }


    @Test
    public void getByTodoIdTaskTest() {
        long todoId = 1;
        ToDo toDo = new ToDo();
        toDo.setId(todoId);

        long id = 1;
        Task task = new Task();
        task.setId(id);
        task.setName("New Task");
        task.setTodo(toDo);

        when(taskRepositoryMock.getByTodoId(todoId)).thenReturn(Collections.singletonList(task));

        List<Task> actual = taskServiceMock.getByTodoId(todoId);

        verify(taskRepositoryMock).getByTodoId(todoId);
        assertEquals(1, actual.size());
        assertEquals(task, actual.get(0));
    }


    @Test
    public void getByTodoIdEmptyListTaskTest() {
        long todoId = 1;

        when(taskRepositoryMock.getByTodoId(todoId)).thenReturn(new ArrayList<>());

        List<Task> actual = taskServiceMock.getByTodoId(todoId);

        verify(taskRepositoryMock).getByTodoId(todoId);
        assertEquals(0, actual.size());
    }


}
