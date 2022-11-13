package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsersRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Transactional
    public void getExistUserTest(){
        Assertions.assertEquals("Nick",userRepository.findByEmail("nick@mail.com").get().getFirstName());
    }
    @Test
    @Transactional
    public void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }
    @Test
    @Transactional
    public void createNewUserTest(){
        User user = new User();
        user.setFirstName("User");
        user.setLastName("New");
        user.setEmail("new@user.com");
        user.setPassword("qwerty123!");
        testEntityManager.persist(user);
        assertThat(user).isEqualTo(userRepository.findByEmail("new@user.com").get());

    }
}
