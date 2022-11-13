package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepositoryMock;

    @InjectMocks
    private ToDoServiceImpl toDoServiceMock;



    @Test
    public void createToDoTest() {
        ToDo toDo = new ToDo();
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.save(toDo)).thenReturn(toDo);

        ToDo actual = toDoServiceMock.create(toDo);

        verify(toDoRepositoryMock).save(toDo);
        assertEquals(toDo, actual);
    }



    @Test
    @Transactional
    public void readByIdToDoTest() {
        long id = 1;
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.findById(toDo.getId())).thenReturn(Optional.of(toDo));

        ToDo actual = toDoServiceMock.readById(id);

        verify(toDoRepositoryMock).findById(toDo.getId());
        assertEquals(toDo, actual);
    }


    @Test
    @Transactional
    public void readByIdNullToDoTest() {
        long id = 2;

        when(toDoRepositoryMock.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> toDoServiceMock.readById(id)
        );

        verify(toDoRepositoryMock).findById(id);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "ToDo with id " + id + " not found");
    }


    @Test
    @Transactional
    public void updateToDoTest() {
        long id = 1;
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.findById(id)).thenReturn(Optional.of(toDo));

        toDo.setTitle("My Updated ToDo #1");

        when(toDoRepositoryMock.save(toDo)).thenReturn(toDo);

        ToDo actual = toDoServiceMock.update(toDo);

        verify(toDoRepositoryMock).findById(id);
        verify(toDoRepositoryMock).save(toDo);
        assertEquals(toDo, actual);
    }


    @Test
    @Transactional
    public void updateNullToDoTest() {
        NullEntityReferenceException thrown = assertThrows(
                NullEntityReferenceException.class,
                () -> toDoServiceMock.update(null)
        );

        assertEquals(NullEntityReferenceException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "ToDo cannot be 'null'");
    }


    @Test
    @Transactional
    public void updateNotExistingToDoTest() {
        long notExistingId = 1;
        ToDo toDo = new ToDo();
        toDo.setId(notExistingId);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("To-Do with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> toDoServiceMock.update(toDo)
        );

        verify(toDoRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "To-Do with id " + notExistingId + " not found");
    }


    @Test
    @Transactional
    public void deleteToDoTest() {
        long id = 1;
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.findById(id)).thenReturn(Optional.of(toDo));

        doNothing().when(toDoRepositoryMock).delete(toDo);

        toDoServiceMock.delete(id);

        verify(toDoRepositoryMock).findById(id);
        verify(toDoRepositoryMock).delete(toDo);
        assertFalse(toDoServiceMock.getAll().contains(toDo));
    }


    @Test
    @Transactional
    public void deleteNotExistingToDoTest() {
        long notExistingId = 1;

        when(toDoRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("To-Do with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> toDoServiceMock.delete(notExistingId)
        );

        verify(toDoRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "To-Do with id " + notExistingId + " not found");
    }


    @Test
    @Transactional
    public void deleteEmptyToDoTest() {
        long id = 1;

        when(toDoRepositoryMock.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> toDoServiceMock.delete(id)
        );

        verify(toDoRepositoryMock).findById(id);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "ToDo with id " + id + " not found");
    }



    @Test
    @Transactional
    public void getAllToDoTest() {
        long id = 1;
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.findAll()).thenReturn(Collections.singletonList(toDo));

        List<ToDo> actual = toDoServiceMock.getAll();

        verify(toDoRepositoryMock).findAll();
        assertEquals(1, actual.size());
    }


    @Test
    @Transactional
    public void getAllEmptyListToDoTest() {
        when(toDoRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        List<ToDo> actual = toDoServiceMock.getAll();

        verify(toDoRepositoryMock).findAll();
        assertEquals(0, actual.size());
    }


    @Test
    @Transactional
    public void getByUserIdToDoTest() {
        long userId = 1;
        long id = 1;
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());

        when(toDoRepositoryMock.getByUserId(userId)).thenReturn(Collections.singletonList(toDo));

        List<ToDo> actual = toDoServiceMock.getByUserId(userId);

        verify(toDoRepositoryMock).getByUserId(userId);
        assertEquals(1, actual.size());
    }


    @Test
    @Transactional
    public void getByUserIdEmptyToDoTest() {
        long userId = 1;

        when(toDoRepositoryMock.getByUserId(userId)).thenReturn(new ArrayList<>());

        List<ToDo> actual = toDoServiceMock.getByUserId(userId);

        verify(toDoRepositoryMock).getByUserId(userId);
        assertEquals(0, actual.size());
    }


}
