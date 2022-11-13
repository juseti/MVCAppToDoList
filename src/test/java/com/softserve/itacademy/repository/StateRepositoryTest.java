package com.softserve.itacademy.repository;


import com.softserve.itacademy.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StateRepositoryTest {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    @Transactional
    public void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }

    @Test
    public void getByNameStateTest() {
        String actual = stateRepository.findByName("Done").getName();
        assertEquals("Done", actual);
    }

    @Test
    public void getAllStateTest() {
        int actual = stateRepository.getAll().size();
        assertEquals(4, actual);
    }


    @Test
    public void saveNewStateTest() {
        State state = new State();
        state.setName("To do");
        testEntityManager.persist(state);
        testEntityManager.flush();
        State actual = stateRepository.findByName("To do");
        assertEquals(state, actual);
    }
}
