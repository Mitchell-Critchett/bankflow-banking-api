package ru.bankflow.account;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Setter
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Setter
    @Column(name = "currency", nullable = false, length = 3)
    private String currencyCode;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public AccountEntity(
            ClientEntity client,
            String currencyCode
    ) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.accountNumber = SecureGenerator.generateSecure20DigitString();
        this.balance = BigDecimal.ZERO;
        this.currencyCode = currencyCode;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = OffsetDateTime.now();
    }

}
