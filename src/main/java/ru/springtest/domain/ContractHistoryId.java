package ru.springtest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ContractHistoryId implements Serializable {
    @Column(name = "contract_id")
    private UUID contractId;

    @Column(name = "history_id")
    private UUID historyId;

    public ContractHistoryId(){}
    public ContractHistoryId(UUID contractId, UUID historyId) {
        this.contractId = contractId;
        this.historyId = historyId;
    }
}
