package ru.bankflow.account;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bankflow.client.ClientEntity;

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
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "currency", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public AccountEntity(
            UUID id,
            ClientEntity client,
            String accountNumber,
            BigDecimal balance,
            String currencyCode,
            AccountStatus status,
            OffsetDateTime createdAt
    ) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currencyCode = currencyCode;
        this.status = status;
        this.createdAt = OffsetDateTime.now();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCreatedAt(OffsetDateTime created_at) {
        this.createdAt = createdAt;
    }
}
