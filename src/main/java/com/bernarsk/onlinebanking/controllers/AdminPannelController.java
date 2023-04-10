//package com.bernarsk.onlinebanking.controllers;
//
//import ch.qos.logback.core.model.Model;
//import com.bernarsk.onlinebanking.models.User;
//import com.bernarsk.onlinebanking.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminPanelController {
//
//    @Autowired
//    private UserService userService; // assuming you have a UserService for managing user accounts
//
//    @GetMapping("/users")
//    public String viewUserList(Model model) {
//        List<User> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        return "admin/user-list";
//    }
//
//    @GetMapping("/users/{id}")
//    public String viewUserDetails(@PathVariable("id") Long id, Model model) {
//        User user = userService.getUserById(id);
//        model.addAttribute("user", user);
//        return "admin/user-details";
//    }
//
//    @GetMapping("/users/{id}/edit")
//    public String editUserForm(@PathVariable("id") Long id, Model model) {
//        User user = userService.getUserById(id);
//        model.addAttribute("user", user);
//        return "admin/edit-user";
//    }
//
//    @PostMapping("/users/{id}/edit")
//    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
//        userService.updateUser(user);
//        return "redirect:/admin/users/" + id;
//    }
//
//    @GetMapping("/users/{id}/delete")
//    public String deleteUser(@PathVariable("id") Long id) {
//        userService.deleteUser(id);
//        return "redirect:/admin/users";
//    }
//
//    // other methods for managing other aspects of the app as needed
//}
