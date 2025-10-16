package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Contracts;

import java.util.UUID;

@Repository
public interface ContractsRepository extends JpaRepository<Contracts, UUID> {
}
