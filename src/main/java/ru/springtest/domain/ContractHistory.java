package ru.springtest.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="contract_history")
public class ContractHistory {
    @EmbeddedId
    private ContractHistoryId id;


    @ManyToOne
    @MapsId("contractId")
    @JoinColumn(name="contract_id")
    private Contracts contract;


    @ManyToOne
    @MapsId("historyId")
    @JoinColumn(name="history_id")
    private History history;

    @Column(name= "event_date")
    private LocalDateTime eventDate;
}
