package org.example.dao;

import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(int id );

}
