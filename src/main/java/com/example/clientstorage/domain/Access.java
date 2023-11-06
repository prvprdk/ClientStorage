package com.example.clientstorage.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Access {
    @Id
    @GeneratedValue
    private Long id;
    private String url;
    private TypeAccesses typeAccesses;
    private String login;
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    private Site site;

}
