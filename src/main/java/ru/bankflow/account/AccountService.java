package ru.bankflow.account;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository repository;

    private final AccountMapper mapper;

    public AccountService(AccountRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Account createAccount(Account accountToCreate) {

        var entityToSave = mapper.toEntity(accountToCreate);
        var savedEntity = repository.save(entityToSave);

        return mapper.toDomain(savedEntity);
    }

    public Account getAccountById(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account with id %s not found".formatted(id)
                ));

        return mapper.toDomain(accountEntity);
    }
}
