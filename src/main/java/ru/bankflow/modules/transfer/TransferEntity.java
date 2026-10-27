package ru.bankflow.modules.transfer;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bankflow.modules.account.AccountEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", nullable = false)
    private AccountEntity sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = false)
    private AccountEntity destinationAccount;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "idempotency_key", nullable = false)
    private UUID idempotencyKey;

    public TransferEntity(
            AccountEntity sourceAccount,
            AccountEntity destinationAccount,
            BigDecimal amount,
            UUID idempotencyKey
    ) {
        this.id = UUID.randomUUID();
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.status = TransferStatus.SUCCEEDED;
        this.createdAt = OffsetDateTime.now();
        this.idempotencyKey = idempotencyKey;
    }
}
