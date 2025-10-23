package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="seller")
@Data
public class Seller {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;
}
