package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.ContractHistory;
import ru.springtest.domain.ContractHistoryId;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractHistoryRepository extends JpaRepository<ContractHistory, ContractHistoryId> {
    List<ContractHistory> findAllByContract_Id(UUID contractId);
    List<ContractHistory> findAllByHistory_Id(UUID historyId);

    boolean existsByContract_IdAndHistory_Id (UUID contractId, UUID historyId);
}
