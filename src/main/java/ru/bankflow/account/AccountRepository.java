package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    boolean existByAccountNumber(String accountNumber);

    @Query("""
        SELECT a.id from accounts a
                WHERE (a.client_id = :client_id)
        """)
    List<AccountEntity> searchAllAccountsByClient(
            @Param("client_id") UUID clientID,
            Pageable pageable
    );


}
