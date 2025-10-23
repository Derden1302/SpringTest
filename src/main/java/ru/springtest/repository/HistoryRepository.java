package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.History;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {
    List<History> findAllByContract_Id(UUID contractId);
}
