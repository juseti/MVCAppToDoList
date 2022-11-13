package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ToDoRepositoryTest {

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    @Transactional
    public void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }


    @Test
    public void getByUserIdTest() {
        int actual = toDoRepository.getByUserId(4).size();
        assertEquals(5, actual);
    }


    @Test
    public void saveNewToDoTest() {
        ToDo toDo = new ToDo();
        toDo.setTitle("My ToDo #1");
        toDo.setCreatedAt(LocalDateTime.now());
        testEntityManager.persist(toDo);
        testEntityManager.flush();
        ToDo actual = toDoRepository.findById(toDo.getId()).orElse(null);
        assertEquals(toDo, actual);
    }

}
