package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void saveUser(User user) throws Exception;

    User getUser(int id);

    void deleteUser(int id);

    List<Role> getListRoles();

    User getCurrentUser();

}
