package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ToDoRepository toDoRepository;
    @Autowired
    StateRepository stateRepository;


    @Test
    @Transactional
    public void findAllTest(){
        List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).isNotEmpty();
        assertThat(taskRepository.findAll()).hasSize(allTasks.size());
    }

    @Test
    @Transactional
    public void updateTaskTest(){

        Task task1 = new Task();
        task1.setName("Task1.1");
        task1.setPriority(Priority.LOW);
        entityManager.persist(task1);

        Task update = taskRepository.findById(task1.getId()).orElse(null);
        update.setName("New Task1.1");
        update.setPriority(Priority.MEDIUM);
        taskRepository.save(update);

        Task check = taskRepository.findById(task1.getId()).orElse(null);
        assertThat(check.getName()).isEqualTo(update.getName());
        assertThat(check.getPriority()).isEqualTo(update.getPriority());

    }
}