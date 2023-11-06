package com.example.clientstorage.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Site {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany (mappedBy = "site", fetch = FetchType.EAGER, orphanRemoval=true)
    private List<Access> accessesSet;

}
