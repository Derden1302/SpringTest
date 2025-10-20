package ru.springtest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContractHistoryId implements Serializable {
    @Column(name = "contract_id")
    private UUID contractId;

    @Column(name = "history_id")
    private UUID historyId;

}
