package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="contracts")
public class Contracts {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "contract")
    private Set<ContractHistory> contractHistories = new HashSet<>();
}
