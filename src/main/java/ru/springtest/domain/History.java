package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "history")
    private Set<ContractHistory> contractHistories = new HashSet<>();
}
