package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Seller;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    @Query("select s from Seller s left join fetch s.item where s.id = :id")
    Optional<Seller> findByIdWithItems(UUID id);
}
