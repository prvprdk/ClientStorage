package com.example.clientstorage.repo;

import com.example.clientstorage.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoClient extends JpaRepository<Client, Long> {

    @Query("from Client e where e.name like concat('%', :filterText, '%')")
    List<Client> findByName(@Param(("filterText")) String filterText);
}
