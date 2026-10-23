package ru.bankflow.account;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private UUID client_id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String account_number;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "currency", nullable = false, length = 3, columnDefinition = "currency")
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "createdAt", nullable = false)
    private OffsetDateTime created_at;

    public AccountEntity(
            UUID id,
            UUID client_id,
            String account_number,
            BigDecimal balance,
            String currencyCode,
            String status,
            OffsetDateTime created_at
    ) {
        this.id = id;
        this.client_id = client_id;
        this.account_number = account_number;
        this.balance = balance;
        this.currencyCode = currencyCode;
        this.status = status;
        this.created_at = created_at;
    }

}
