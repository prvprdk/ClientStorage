package com.example.clientstorage.repo;

import com.example.clientstorage.domain.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoAccesses extends JpaRepository<Access, Long> {



}
