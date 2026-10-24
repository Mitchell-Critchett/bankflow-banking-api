package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import ru.bankflow.client.ClientEntity;

import java.util.List;
import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    boolean existsByAccountNumber(String accountNumber);

    List<AccountEntity> searchAccountsByClientId(ClientEntity client_id, Pageable pageable);


}
