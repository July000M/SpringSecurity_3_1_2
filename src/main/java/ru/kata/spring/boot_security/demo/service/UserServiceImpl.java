package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UsersRepository;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository usersRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        String userPassword = user.getPassword();
        if (Objects.isNull(userPassword) || Objects.equals("", userPassword)) {
            String oldPassword = usersRepository.findById(user.getId())
                    .map(User::getPassword)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(oldPassword);
        } else {
            encodePassword(user);
        }
        usersRepository.save(user);
    }

    @Override
    public List<Role> getListRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return usersRepository.findUserByName(currentPrincipalName);
    }

    @Override
    public User getUser(int id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }


    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

}
