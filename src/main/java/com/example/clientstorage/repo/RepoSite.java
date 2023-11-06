package com.example.clientstorage.repo;

import com.example.clientstorage.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoSite extends JpaRepository<Site, Long> {


}
