package com.smartcityx.controller.api;

import com.smartcityx.entity.User;
import com.smartcityx.entity.Role;
import com.smartcityx.repository.UserRepository;
import com.smartcityx.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public ApiUserController(UserRepository userRepository, RoleRepository roleRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode("password123")); // Default password if not provided
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Ensure roles are fetched from DB to avoid transient instances
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Role> attachedRoles = new HashSet<>();
            for (Role role : user.getRoles()) {
                roleRepository.findByName(role.getName()).ifPresent(attachedRoles::add);
            }
            user.setRoles(attachedRoles);
        }
        
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(userDetails.getFullName());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());
            user.setEnabled(userDetails.isEnabled());
            
            if (userDetails.getRoles() != null) {
                Set<Role> attachedRoles = new HashSet<>();
                for (Role role : userDetails.getRoles()) {
                    roleRepository.findByName(role.getName()).ifPresent(attachedRoles::add);
                }
                user.setRoles(attachedRoles);
            }
            
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
