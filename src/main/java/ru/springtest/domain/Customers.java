package ru.springtest.domain;

import jakarta.persistence.*;

import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="customers")
@Data
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Accounts account;
}
