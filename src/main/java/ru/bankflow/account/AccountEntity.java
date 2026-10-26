package ru.bankflow.account;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bankflow.client.ClientEntity;
import ru.bankflow.exceptions.AccountNotActiveException;
import ru.bankflow.transfers.exceptions.InsufficientFundsException;

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

    public void credit(
            BigDecimal amount
    ) {
        if (!status.equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException("Аккаунт отправитель неактивный");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательная сумма перевода");
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств для перевода");
        }

        balance = balance.subtract(amount);
    }

    public void debit(
            BigDecimal amount
    ) {
        if (!status.equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException("Аккаунт получатель неактивный");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательная сумма перевода");
        }

        balance = balance.add(amount);
    }


}
