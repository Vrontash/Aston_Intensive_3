package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoTest {

    @Container
    private static final  PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void setup() {
        HibernateUtil.createSessionFactory(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        userDao = new UserDaoImpl();
    }

    @BeforeEach
    void deleteUserData() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }

    }

    @Test
    void save_user_in_db_and_generate_id_is_success() {
        User user = new User("TestS", "testS@test.ru", 24);
        userDao.save(user);
        assertTrue(user.getId() > 0);
        Optional<User> foundUser = userDao.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("TestS", foundUser.get().getName());
    }

    @Test
    void find_user_by_id_is_success(){
        User user = new User("TestF", "testF@test.ru", 34);
        userDao.save(user);
        Optional<User> foundUser = userDao.findById(user.getId());
        assertTrue(foundUser.isPresent());
    }
    @Test
    void find_user_by_non_existent_id_is_empty(){
        Optional<User> foundUser = userDao.findById(23);
        assertTrue(foundUser.isEmpty());
    }
    @Test
    void find_all_users_is_success(){
        userDao.save(new User("TestFA1", "testFA1@test.ru", 44));
        userDao.save(new User("TestFA2", "testFA2@test.ru", 45));
        userDao.save(new User("TestFA3", "testFA3@test.ru", 46));

        List<User> users = userDao.findAll();
        assertEquals(3, users.size());
        assertEquals("TestFA1", users.get(0).getName());
        assertEquals("TestFA2", users.get(1).getName());
        assertEquals("TestFA3", users.get(2).getName());
    }
    @Test
    void update_user_is_success(){
        User user = new User("TestU", "testU@test.ru", 54);
        userDao.save(user);
        user.setName("TestU2");
        user.setAge(55);

        userDao.update(user);
        Optional<User> uUser = userDao.findById(user.getId());
        assertTrue(uUser.isPresent());
        assertEquals(uUser.get().getName(), "TestU2");
        assertEquals(uUser.get().getAge(), 55);
    }
    @Test
    void delete_user_is_success(){
        User user = new User("TestD", "testD@test.ru", 64);
        userDao.save(user);

        userDao.delete(user.getId());
        Optional<User> dUser = userDao.findById(user.getId());
        assertTrue(dUser.isEmpty());
    }



}