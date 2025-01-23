package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.UserDTO;
import ru.kata.spring.boot_security.demo.service.CustomUserDetailsService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final RoleService roleService;
    private final CustomUserDetailsService customUserDetailsService;

    public ApiController(UserService userService, RoleService roleService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/role/id/{id}")
    public Role getRole(@PathVariable("id") Long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/role/name/{roleName}")
    public Role getRoleByName(@PathVariable("roleName") String roleName)
    {
        for(Role role : roleService.getAllRoles()) {
            if(Objects.equals(role.getRoleName(), roleName)) {
                return role;
            }
        }
        return null;
    }

    @GetMapping("/current")
    public User getCurrent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ) {
            UserDetails userDetails = (UserDetails) principal;
            return customUserDetailsService.loadUserByUsername(userDetails.getUsername());
        } else {
            String username = principal.toString();
            return customUserDetailsService.loadUserByUsername(username);
        }
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<User> setUser(@RequestBody UserDTO userDTO) {
        User user = userService.toUser(userDTO);
        List<Long> roleIds = new ArrayList<>();
        if (userDTO.getRoles() == null) {
            userDTO.setRoles(new HashSet<>());
        }
        for (Integer roleId : userDTO.getRoleIds() ) {
            roleIds.add(Long.parseLong(roleId.toString()));
        }
        userService.updateUser(user, roleIds , userDTO.getPassword());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO) {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setLastname(userDTO.getLastname());
        newUser.setAge(userDTO.getAge());
        newUser.setEmail(userDTO.getEmail());
        newUser.setId(userDTO.getId());
        newUser.setPassword(userDTO.getPassword());
        int i = userDTO.getRoleIds().get(0);
        userService.saveUser(newUser, roleService.getRoleById(Long.parseLong( Integer.toString(i))).getRoleName());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
