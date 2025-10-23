package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Seller;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
}
