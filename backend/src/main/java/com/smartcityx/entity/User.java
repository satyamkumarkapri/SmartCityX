package com.smartcityx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_user_username", columnList = "username")})
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 80)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String fullName;

    @Email
    @Column(length = 150, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 250)
    private String address;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
