package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="contract")
public class Contract {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
            name = "contract_history",                   // та же таблица
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id")
    )
    private Set<History> history;
}
