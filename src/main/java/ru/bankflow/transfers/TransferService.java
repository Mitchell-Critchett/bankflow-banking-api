package ru.bankflow.transfers;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bankflow.account.AccountEntity;
import ru.bankflow.account.AccountRepository;
import ru.bankflow.transfers.dto.TransferCreateRequest;
import ru.bankflow.transfers.dto.TransferResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    @Transactional
    public TransferResponse createTransfer(
            TransferCreateRequest transferToCreate,
            UUID idempotencyKey
    ) {
        AccountEntity sourceAccount = accountRepository.findById(transferToCreate.sourceAccount())
                .orElseThrow(() -> new EntityNotFoundException(
                        "аккаунт отправителя с id %s не найден".formatted(transferToCreate.sourceAccount()
                        )));

        AccountEntity destinationAccount = accountRepository.findById(transferToCreate.destinationAccount())
                .orElseThrow(() -> new EntityNotFoundException(
                        "аккаунт отправителя с id %s не найден".formatted(transferToCreate.destinationAccount()
                        )));

        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new IllegalArgumentException("Аккаунт оправитель и получатель должны различаться!");
        }

        sourceAccount.credit(transferToCreate.amount());
        destinationAccount.debit(transferToCreate.amount());

        var savedSourceAccount = accountRepository.save(sourceAccount);
        var savedDestinationAccount = accountRepository.save(destinationAccount);

        TransferEntity entityToSave = new TransferEntity(
                savedSourceAccount,
                savedDestinationAccount,
                transferToCreate.amount(),
                idempotencyKey
        );

        return toResponse(
                entityToSave,
                savedSourceAccount.getId(),
                savedDestinationAccount.getId()
        );
    }

    private TransferResponse toResponse(
            TransferEntity transferEntity,
            UUID sourceAccountId,
            UUID destinationAccountId)
    {
        return new TransferResponse(
                transferEntity.getId(),
                sourceAccountId,
                destinationAccountId,
                transferEntity.getAmount(),
                transferEntity.getStatus(),
                transferEntity.getCreatedAt(),
                transferEntity.getIdempotencyKey()
        );
    }
}
