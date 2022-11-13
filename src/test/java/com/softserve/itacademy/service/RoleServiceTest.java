package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepositoryMock;

    @InjectMocks
    private RoleServiceImpl roleServiceMock;


    @Test
    public void createRoleTest() {
        Role role = new Role();
        role.setName("User");

        when(roleRepositoryMock.save(role)).thenReturn(role);

        Role actual = roleServiceMock.create(role);

        verify(roleRepositoryMock).save(role);
        assertEquals(role, actual);
    }


    @Test
    public void readByIdRoleTest() {
        long id = 1;
        Role role = new Role();
        role.setId(id);
        role.setName("User");

        when(roleRepositoryMock.findById(role.getId())).thenReturn(Optional.of(role));

        Role actual = roleServiceMock.readById(id);

        verify(roleRepositoryMock).findById(role.getId());
        assertEquals(role, actual);
    }


    @Test
    public void readByNotExistingIdRoleTest() {
        long notExistingId = 2;

        when(roleRepositoryMock.findById(notExistingId))
                .thenReturn(Optional.empty());

        EntityNotFoundException  thrown = assertThrows(
                EntityNotFoundException.class,
                () -> roleServiceMock.readById(notExistingId)
        );

        verify(roleRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
    }


    @Test
    public void updateRoleTest() {
        long id = 1;
        Role role = new Role();
        role.setId(id);
        role.setName("User");

        when(roleRepositoryMock.findById(id)).thenReturn(Optional.of(role));

        role.setName("Advanced User");

        when(roleRepositoryMock.save(role)).thenReturn(role);

        Role actual = roleServiceMock.update(role);

        verify(roleRepositoryMock).findById(id);
        verify(roleRepositoryMock).save(role);
        assertEquals(role, actual);
    }


    @Test
    public void updateNullRoleTest() {
        NullEntityReferenceException thrown = assertThrows(
                NullEntityReferenceException.class,
                () -> roleServiceMock.update(null)
        );

        assertEquals(NullEntityReferenceException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Role cannot be 'null'");
    }



    @Test
    public void updateNotExistingRoleTest() {
        long notExistingId = 1;
        Role role = new Role();
        role.setId(notExistingId);
        role.setName("User #1");

        when(roleRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("Role with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> roleServiceMock.update(role)
        );

        verify(roleRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Role with id " + notExistingId + " not found");
    }


    @Test
    public void deleteRoleTest() {
        long id = 1;
        Role role = new Role();
        role.setId(id);
        role.setName("User");

        when(roleRepositoryMock.findById(id)).thenReturn(Optional.of(role));

        doNothing().when(roleRepositoryMock).delete(role);

        roleServiceMock.delete(id);

        verify(roleRepositoryMock).findById(id);
        verify(roleRepositoryMock).delete(role);
        assertFalse(roleServiceMock.getAll().contains(role));
    }


    @Test
    public void deleteNotExistingRoleTest() {
        long notExistingId = 1;

        when(roleRepositoryMock.findById(notExistingId))
                .thenThrow(new EntityNotFoundException("Role with id " + notExistingId + " not found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> roleServiceMock.delete(notExistingId)
        );

        verify(roleRepositoryMock).findById(notExistingId);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "Role with id " + notExistingId + " not found");
    }


    @Test
    public void deleteNullRoleTest() {
        long id = 1;

        when(roleRepositoryMock.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> roleServiceMock.delete(id)
        );

        verify(roleRepositoryMock).findById(id);
        assertEquals(EntityNotFoundException.class, thrown.getClass());
    }



    @Test
    public void getAllRoleTest() {
        long id = 1;
        Role role = new Role();
        role.setId(id);
        role.setName("User");

        when(roleRepositoryMock.findAll()).thenReturn(Collections.singletonList(role));

        List<Role> actual = roleServiceMock.getAll();

        verify(roleRepositoryMock).findAll();
        assertEquals(1, actual.size());
    }


    @Test
    public void getAllEmptyListRoleTest() {
        when(roleRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        List<Role> actual = roleServiceMock.getAll();

        verify(roleRepositoryMock).findAll();
        assertTrue(actual.isEmpty());
    }


}

