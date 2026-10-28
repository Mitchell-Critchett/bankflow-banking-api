package ru.bankflow.modules.account;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankflow.exceptions.AccountNotActiveException;
import ru.bankflow.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountRepository accountRepository;

    public void processDebit(AccountEntity account, BigDecimal amount) {

        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException("Аккаунт отправитель неактивный");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательная сумма перевода");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств для перевода");
        }

        account.debit(amount);
        accountRepository.save(account);
    }

    public void processCredit(AccountEntity account, BigDecimal amount) {

        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException("Аккаунт получатель неактивный");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательная сумма перевода");
        }

        account.credit(amount);
        accountRepository.save(account);
    }
}
