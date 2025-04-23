package org.example;

import org.example.dao.UserDaoImpl;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest{
    @Mock
    private UserDaoImpl userDaoMock;
    @InjectMocks
    private UserService userService;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream));
    }
    @AfterEach
    void resetMock() {
        Mockito.reset(userDaoMock);
    }
    private Scanner createScanner(String input){
        InputStream is = new ByteArrayInputStream(input.getBytes());
        return new Scanner(is);
    }

    @Test
    public void create_user_with_correct_data_is_successful(){
        String input = "1\nTest\ntest@test.ru\n23\n6"; //Эмуляция вводимых пользователем данных
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User created with Id: ")); //Проверка на вывод нужного текста
        verify(userDaoMock, times(1)).save(any()); // Проверка на вызов метода UserDao.save()
    }
    @Test
    public void updateUser_with_correct_data_is_successful(){
        User testUser = new User("TestU", "testU@test.ru", 23);
        testUser.setId(1);
        String input = "4\n1\nTested\ntested@test.ru\n24\n6";

        // Используем мокито, чтобы он при запросе возвращал user'а без использования бд
        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User updated"));
        verify(userDaoMock, times(1)).update(any(User.class));
    }
    @Test
    public void updateUser_without_changing_data_is_successful(){
        User testUser = new User("TestU", "testU@test.ru", 23);
        testUser.setId(1);
        String input = "4\n1\n\n\n\n6";

        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User updated"));
        verify(userDaoMock, times(1)).update(any(User.class));
    }
    @Test
    public void updateUser_with_invalid_age_is_prohibited(){
        User testUser = new User("TestU", "testU@test.ru", 23);
        testUser.setId(1);
        String input = "4\n1\nTested\ntested@test.ru\nInvalidAge\n6";

        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("Invalid age!"));
        verify(userDaoMock, never()).update(any(User.class));
    }
    @Test
    public void updateUser_with_invalid_email_is_prohibited(){
        User testUser = new User("TestU", "testU@test.ru", 23);
        testUser.setId(1);
        String input = "4\n1\nTested\ninvalidEmail\n24\n6";

        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("Invalid Email!"));
        verify(userDaoMock, never()).update(any(User.class));
    }
    @Test
    public void delete_created_user_is_successful(){
        User testUser = new User("TestD", "testD@test.ru", 33);
        testUser.setId(1);
        String input = "5\n1\n6";

        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User deleted"));
        verify(userDaoMock, times(1)).delete(1);
    }
    @Test
    public void delete_desired_user_isnt_found(){
        String input = "5\n1\n6";
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User not found!"));
        verify(userDaoMock, never()).delete(1);
    }
    @Test
    public void find_user_by_correct_id_is_successful(){
        User testUser = new User("TestF", "testF@test.ru", 43);
        testUser.setId(1);
        String input = "3\n1\n6";

        when(userDaoMock.findById(1)).thenReturn(Optional.of(testUser));
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User found:"));
        verify(userDaoMock, times(1)).findById(1);
    }
    @Test
    public void find_user_by_nonexistent_id_isnt_found(){
        String input = "3\n1\n6";
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("User not found!"));
        verify(userDaoMock, times(1)).findById(1);
    }
    @Test
    public void find_all_users_is_successful(){
        String input = "2\n6";
        userService.setScanner(createScanner(input));
        userService.run();
        verify(userDaoMock, times(1)).findAll();
    }
    @Test
    public void create_user_with_invalid_age_is_prohibited(){
        String input = "1\nTest\ntest@test.ru\nInvalidAge\n6";
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("Invalid age!"));
        verify(userDaoMock, never()).save(any());
    }
    @Test
    public void create_user_with_invalid_email_is_prohibited(){
        String input = "1\nTest\nInvalidEmail\n6";
        userService.setScanner(createScanner(input));
        userService.run();
        assertTrue(outputStream.toString().contains("Invalid Email!"));
        verify(userDaoMock, never()).save(any());

    }
}