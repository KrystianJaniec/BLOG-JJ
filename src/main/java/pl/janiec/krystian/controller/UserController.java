package pl.janiec.krystian.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import pl.janiec.krystian.dto.UserDTO;

import pl.janiec.krystian.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerNewUser(Model model) {
        model.addAttribute("view", "user/register");
        return "layout";
    }

    @PostMapping("/register")
    public String registerNewUserProcess(@Valid UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return "redirect:/register";
        }
        userService.create(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginUser(Model model) {
        model.addAttribute("view", "user/login");
        return "layout";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    @PreAuthorize("authenticated")
    public String userProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userService.profile(authentication));
        model.addAttribute("view", "user/profile");
        return "layout";
    }

}
