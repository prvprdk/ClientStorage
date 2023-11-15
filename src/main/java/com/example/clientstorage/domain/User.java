package com.example.clientstorage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity(name = "usr")
@Data
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, insertable = true)
    private String username;
    private String password;
}
