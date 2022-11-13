package com.softserve.itacademy.service;


import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.impl.StateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StateServiceTest {

    @Mock
    private StateRepository stateRepositoryMock;

    @InjectMocks
    private StateServiceImpl stateServiceMock;



    @Test
    public void createStateTest() {
        State state = new State();
        state.setName("To Do");

        when(stateRepositoryMock.save(state)).thenReturn(state);

        State actual = stateServiceMock.create(state);

        verify(stateRepositoryMock).save(state);
        assertEquals(state, actual);
    }



    @Test
    public void readByIdStateTest() {
        long id = 1;
        State state = new State();
        state.setId(id);
        state.setName("To do");

        when(stateRepositoryMock.findById(state.getId())).thenReturn(Optional.of(state));

        State actual = stateServiceMock.readById(id);

        verify(stateRepositoryMock).findById(state.getId());
        assertEquals(state, actual);
    }


    @Test
    public void readByNotExistingIdStateTest() {
        long notExistingId = 2;

        when(stateRepositoryMock.findById(notExistingId))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> stateServiceMock.readById(notExistingId)
        );

        verify(stateRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State with id " + notExistingId + " not found");
    }


    @Test
    public void updateStateTest() {
        long id = 1;
        State state = new State();
        state.setId(id);
        state.setName("To do");

        when(stateRepositoryMock.findById(id)).thenReturn(Optional.of(state));

        state.setName("To do #1");

        when(stateRepositoryMock.save(state)).thenReturn(state);

        State actual = stateServiceMock.update(state);

        verify(stateRepositoryMock).findById(id);
        verify(stateRepositoryMock).save(state);
        assertEquals(state, actual);
    }


    @Test
    public void updateNullStateTest() {
        NullEntityReferenceException thrown = assertThrows(
                NullEntityReferenceException.class,
                () -> stateServiceMock.update(null)
        );

        assertEquals(NullEntityReferenceException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State cannot be 'null'");
    }



    @Test
    public void updateNotExistingStateTest() {
        long notExistingId = 1;
        State state = new State();
        state.setId(notExistingId);
        state.setName("To do #1");

        when(stateRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("State with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> stateServiceMock.update(state)
        );

        verify(stateRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State with id " + notExistingId + " not found");
    }


    @Test
    public void deleteStateTest() {
        long id = 1;
        State state = new State();
        state.setId(id);
        state.setName("To do");

        when(stateRepositoryMock.findById(id)).thenReturn(Optional.of(state));

        doNothing().when(stateRepositoryMock).delete(state);

        stateServiceMock.delete(id);

        verify(stateRepositoryMock).findById(id);
        verify(stateRepositoryMock).delete(state);
        assertFalse(stateServiceMock.getAll().contains(state));
    }


    @Test
    public void deleteNotExistingStateTest() {
        long notExistingId = 1;

        when(stateRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("State with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> stateServiceMock.delete(notExistingId)
        );

        verify(stateRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State with id " + notExistingId + " not found");
    }


    @Test
    public void deleteNullStateTest() {
        long id = 1;

        when(stateRepositoryMock.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> stateServiceMock.delete(id)
        );

        verify(stateRepositoryMock).findById(id);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State with id " + id + " not found");
    }



    @Test
    public void getAllStateTest() {
        long id = 1;
        State state = new State();
        state.setId(id);
        state.setName("To do");

        when(stateRepositoryMock.getAll()).thenReturn(Collections.singletonList(state));

        List<State> actual = stateServiceMock.getAll();

        verify(stateRepositoryMock).getAll();
        assertEquals(1, actual.size());
    }


    @Test
    public void getAllEmptyListStateTest() {
        when(stateRepositoryMock.getAll()).thenReturn(new ArrayList<>());

        List<State> actual = stateServiceMock.getAll();

        verify(stateRepositoryMock).getAll();
        assertTrue(actual.isEmpty());
    }


    @Test
    public void getByNameStateTest() {
        long userId = 1;
        long id = 1;
        State state = new State();
        state.setId(id);
        state.setName("To do");

        when(stateRepositoryMock.findByName(state.getName())).thenReturn(state);

        State actual = stateServiceMock.getByName(state.getName());

        verify(stateRepositoryMock).findByName(state.getName());
        assertEquals(state.getName(), actual.getName());
        assertEquals(state, actual);
    }


    @Test
    public void getByNameNotExistingStateTest() {
        String name = "To do";

        when(stateRepositoryMock.findByName(name)).thenReturn(null);

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> stateServiceMock.getByName(name)
        );

        verify(stateRepositoryMock).findByName(name);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "State with name '" + name + "' not found");
    }

}
