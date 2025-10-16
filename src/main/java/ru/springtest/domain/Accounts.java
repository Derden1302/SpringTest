package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="accounts")
public class Accounts {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name= "account_data")
    private String accountData;

    @OneToOne
    @JoinColumn(name= "customer_id", unique = true)
    private Customers customer;
}