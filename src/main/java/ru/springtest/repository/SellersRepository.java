package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Sellers;

import java.util.UUID;

@Repository
public interface SellersRepository extends JpaRepository<Sellers, UUID> {
}
