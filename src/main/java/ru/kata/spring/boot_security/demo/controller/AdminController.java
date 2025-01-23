package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.CustomUserDetailsService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final CustomUserDetailsService customUserDetailsService;

    public AdminController(UserService userService, RoleService roleService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User currentUser = customUserDetailsService.loadUserByUsername(userDetails.getUsername());
        User newUser = new User();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("usersList", userService.showAllUsers());
        model.addAttribute("addedUser", newUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "adminPage";
    }

    @GetMapping("/admin/edit/")
    public ModelAndView editPage(@RequestParam String id) {
        User user = userService.findUserById(Long.parseLong(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editPage");
        modelAndView.addObject("user", user);
        modelAndView.addObject("availableRoles",roleService.getAllRoles());
        return modelAndView;
    }

    @PostMapping(value = "/admin/edit/")
    public ModelAndView editUser(@ModelAttribute("user") @Valid User user, BindingResult result, @RequestParam("roles") List<Long> roleIds, @RequestParam(value = "password", required = false) String password) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("editPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        userService.updateUser(user, roleIds, password);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }

    @GetMapping("/admin/add")
    public ModelAndView addPage() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addNewUserPage");
        modelAndView.addObject("user", user);
        List<Role> availableRoles = roleService.getAllRoles();
        modelAndView.addObject("availableRoles", availableRoles);
        return modelAndView;
    }

    @PostMapping(value = "/admin/add/")
    public ModelAndView addUser(@ModelAttribute("addedUser") @Valid User user, BindingResult result, @RequestParam String mySelect) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("addNewUserPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        try {
            userService.saveUser(user, mySelect);
            modelAndView.setViewName("redirect:/admin");
        } catch (IllegalArgumentException e) {
            result.addError(new ObjectError("username", e.getMessage()));
            modelAndView.setViewName("addNewUserPage");
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @PostMapping(value = "/admin/delete/")
    public ModelAndView deleteUser(@RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin");
        userService.deleteUser(Long.parseLong(id));
        return modelAndView;
    }
}
