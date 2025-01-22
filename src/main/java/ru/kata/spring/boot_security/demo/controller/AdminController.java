package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private String userNameBuff;

    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User currentUser = (User)userService.loadUserByUsername(userDetails.getUsername());
        User newUser = new User();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("usersList", userService.showAllUsers());
        model.addAttribute("addedUser", newUser);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "adminPage";
    }

    @GetMapping("/admin/edit/")
    public ModelAndView editPage(@RequestParam String id) {
        User user = userService.findUserById(Long.parseLong(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editPage");
        modelAndView.addObject("user", user);
        userNameBuff = user.getUsername();
        Set<Role> roles = user.getRoles();
        modelAndView.addObject("availableRoles", userService.getAllRoles());
        for (Role r : roles) {
            if (Objects.equals(r.getRoleName(), "ROLE_USER")) {
                user.setUserRoleIsSet(true);
            }
            if (Objects.equals(r.getRoleName(), "ROLE_ADMIN")) {
                user.setAdminRoleIsSet(true);
            }
        }
        return modelAndView;
    }

    @PostMapping(value = "/admin/edit/")
    public ModelAndView editUser(@ModelAttribute("user") @Valid User user, BindingResult result, @RequestParam("roles") List<Long> roleIds, @RequestParam(value = "password", required = false) String password) {
        ModelAndView modelAndView = new ModelAndView();
        //if (!Objects.equals(userNameBuff, user.getUsername())) {
//            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
//            if (userDetails != null) {
//                result.addError(new ObjectError("username", "Username is not unique!"));
//            }
        //}
        if (result.hasErrors()) {
            modelAndView.setViewName("editPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
/*        User trueUser = userService.findUserById(user.getId());
        Set<Role> roles = trueUser.getRoles();
        Role adminRole = userService.getRoleById(2L);
        Role userRole = userService.getRoleById(1L);
        if (user.getUserRoleIsSet()) {
            roles.add(userRole);
        } else {
            roles.remove(userRole);
        }
        if (user.getAdminRoleIsSet()) {
            roles.add(adminRole);
        } else {
            roles.remove(adminRole);
        }*/
        Set<Role> roles = new HashSet<>();
            for( long id : roleIds) {
            Role role = userService.getRoleById(id);
            roles.add(role);
        }
        user.setRoles(roles);
        if (password != null && !password.isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
        } else {
            User existingUser = userService.findUserById(user.getId());
            user.setPassword(existingUser.getPassword());
        }
        userService.updateUser(user);
//      userService.saveUser(user);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }

    @GetMapping("/admin/add")
    public ModelAndView addPage() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addNewUserPage");
        modelAndView.addObject("user", user);
        List<Role> availableRoles = userService.getAllRoles();
        modelAndView.addObject("availableRoles", availableRoles);
        return modelAndView;
    }

    @PostMapping(value = "/admin/add/")
    public ModelAndView addUser(@ModelAttribute("addedUser") @Valid User user, BindingResult result, @RequestParam String mySelect) {
        ModelAndView modelAndView = new ModelAndView();
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignore) {

        }
        if (userDetails != null) {
            result.addError(new ObjectError("username", "Username is not unique!"));
        }
        if (result.hasErrors()) {
            modelAndView.setViewName("addNewUserPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        Set<Role> roles = new HashSet<>();
        if( mySelect.equals("2")) {
            Role userRole = userService.getRoleById(1L);
            roles.add(userRole);
        }
        if( mySelect.equals("1")) {
            Role adminRole = userService.getRoleById(2L);
            roles.add(adminRole);
        }
        user.setRoles(roles);
        userService.saveUser(user);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }

    @GetMapping(value = "/admin/delete/")
    public ModelAndView deleteUser(@RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin");
        userService.deleteUser(Long.parseLong(id));
        return modelAndView;
    }
}
