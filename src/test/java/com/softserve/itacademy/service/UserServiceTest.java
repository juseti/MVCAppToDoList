package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private UserServiceImpl userServiceMock;


    @Test
    public void updateEmptyUserTest(){
        NullEntityReferenceException thrown = assertThrows(
                NullEntityReferenceException.class,
                () -> userServiceMock.update(null)
        );

        assertEquals(NullEntityReferenceException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "User cannot be 'null'");
    }


    @Test
    public void deleteNotExistUserTest(){
        long id = 20L;
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> userServiceMock.delete(id)
        );
        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "User with id " + id + " not found");
    }


    @Test
    public void readNotExistUserTest(){
        long id = 20;
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> userServiceMock.readById(id)
        );

        assertEquals(EntityNotFoundException.class, thrown.getClass());
        assertEquals(thrown.getMessage(), "User with id " + id + " not found");
    }


    @Test
    public void getAllEmptyListUserTest() {
        when(userRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        List<User> actual = userServiceMock.getAll();

        verify(userRepositoryMock).findAll();
        assertEquals(0, actual.size());
    }

}

