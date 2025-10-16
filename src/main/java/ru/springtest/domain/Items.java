package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="items")
@Data
public class Items {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="seller_id", referencedColumnName = "id")
    private Sellers seller;
}
