package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;

@Controller
public class MyController {

    @Autowired
    private final UserService userService;

    public MyController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String hello() {
        return "index";
    }
    @GetMapping("/user")
    public String user(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User)userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userName", userDetails.getUsername());
        model.addAttribute("userPassword", userDetails.getPassword());
        return "userPage";
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("usersList", userService.showAllUsers());
        return "adminPage";
    }
    @GetMapping("/admin/edit/")
    public ModelAndView editPage(@RequestParam String id) {
        User user = userService.findUserById(Long.parseLong(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editPage");
        modelAndView.addObject("user", user);
        String confirm1 = null;
        modelAndView.addObject("confirm1", confirm1);
        return modelAndView;
    }
    @PostMapping(value="/admin/edit/")
    public ModelAndView editUser(@ModelAttribute("user") @Valid User user, BindingResult result, @ModelAttribute("confirm1") String confirm1){
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors()) {
            modelAndView.setViewName("editPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        if(!confirm1.isBlank()) {
            //стоит админская роль
            userService.saveAdmin(user);
        } else {
            userService.saveUser(user);
        }
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }
    @GetMapping("/admin/add")
    public ModelAndView addPage() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editPage");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @PostMapping(value="/admin/add/")
    public ModelAndView addUser(@ModelAttribute("user") @Valid User user, BindingResult result, @ModelAttribute("confirm1") String confirm1) {
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors()) {
            modelAndView.setViewName("editPage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        if(!confirm1.isBlank()) {
            userService.saveAdmin(user);
        } else {
            userService.saveUser(user);
        }
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }
    @GetMapping(value="/admin/delete/")
    public ModelAndView deleteUser(@RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin");
        userService.deleteUser(Long.parseLong(id));
        return modelAndView;
    }
}
