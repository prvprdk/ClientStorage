package com.example.clientstorage.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Client {
   @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String number;
    private String site;

}
