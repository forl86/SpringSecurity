package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final CustomUserDetailsService customUserDetailsService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            throw new EntityNotFoundException("No user with such id!");
        }
        return userFromDb.orElse(new User());
    }

    @Override
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user, String roleSelect) {
        UserDetails userDetails = null;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignore) {
            // Игнорируем, если пользователь не найден
        }
        if (userDetails != null) {
            throw new IllegalArgumentException("Username is not unique!");
        }
        Set<Role> roles = new HashSet<>();
        if (roleSelect.equals("2")) {
            Role userRole = roleService.getRoleById(1L);
            roles.add(userRole);
        } else if (roleSelect.equals("1")) {
            Role adminRole = roleService.getRoleById(2L);
            roles.add(adminRole);
        }
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
    @Override
    public void updateUser(User user, List<Long> roleIds, String password) {
        Set<Role> roles = new HashSet<>();
        for( long id : roleIds) {
            Role role = roleService.getRoleById(id);
            roles.add(role);
        }
        user.setRoles(roles);
        if (password != null && !password.isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
        } else {
            User existingUser = this.findUserById(user.getId());
            user.setPassword(existingUser.getPassword());
        }
        User existingUser = this.findUserById(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRoles(user.getRoles());
        existingUser.setAge(user.getAge());
        existingUser.setLastname(user.getLastname());
        if( user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        userRepository.save(existingUser);
    }
}
