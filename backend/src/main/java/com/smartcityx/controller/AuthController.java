package com.smartcityx.controller;

import com.smartcityx.entity.Role;
import com.smartcityx.entity.User;
import com.smartcityx.repository.RoleRepository;
import com.smartcityx.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user, Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email is already registered.");
            return "auth/signup";
        }
        
        user.setUsername(user.getEmail()); // Set username to email since we are using email for login
        
        Role citizenRole = roleRepository.findByName("CITIZEN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("CITIZEN");
                    return roleRepository.save(r);
                });
                
        user.getRoles().add(citizenRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
        
        return "redirect:/login?registered=true";
    }

    @GetMapping("/")
    public String root() {
        return "landing";
    }
}
