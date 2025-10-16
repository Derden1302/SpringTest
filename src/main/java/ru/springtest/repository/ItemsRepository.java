package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Items;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsRepository extends JpaRepository<Items, UUID> {
    List<Items> findBySellerId(UUID sellerId);
}
