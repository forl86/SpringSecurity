package ru.kata.spring.boot_security.demo.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {
    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();
    @JsonDeserialize(using = RoleIdsDeserializer.class)
    private List<Integer> roleIds;
    private String lastname;
    private long id;
    private int age;
    private String email;

    public List<Integer> getRoleIds() {
        return this.roleIds;
    }

    public void setRoleIds(List<Integer> roleIds)
    {
        this.roleIds = roleIds;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
