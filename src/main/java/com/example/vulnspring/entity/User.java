package com.example.vulnspring.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String username;
    private String password;
    private String name;
    private String accountnumber;
    private Float balance;
}
