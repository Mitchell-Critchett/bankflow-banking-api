package ru.bankflow.modules.ledger;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bankflow.modules.account.AccountEntity;
import ru.bankflow.modules.transfer.TransferEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_entries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(name = "flow_funds", nullable = false, precision = 19, scale = 2)
    private BigDecimal flowFunds;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private TransferEntity transfer;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "idempotency_key", nullable = false)
    private UUID idempotencyKey;

    public EntryEntity(AccountEntity account, BigDecimal flowFunds, OperationType operationType, TransferEntity transfer, UUID idempotencyKey) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.flowFunds = flowFunds;
        this.operationType = operationType;
        this.transfer = transfer;
        this.createdAt = OffsetDateTime.now();
        this.idempotencyKey = idempotencyKey;
    }

    public EntryEntity(AccountEntity account, BigDecimal flowFunds, OperationType operationType, UUID idempotencyKey) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.flowFunds = flowFunds;
        this.operationType = operationType;
        this.createdAt = OffsetDateTime.now();
        this.idempotencyKey = idempotencyKey;
    }
}
