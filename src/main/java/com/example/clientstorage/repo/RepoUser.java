package com.example.clientstorage.repo;


import com.example.clientstorage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface RepoUser extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
