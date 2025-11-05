package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
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

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Item> item;
}
