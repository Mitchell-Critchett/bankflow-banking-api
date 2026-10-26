package ru.bankflow.transfers;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bankflow.account.AccountEntity;
import ru.bankflow.account.AccountRepository;
import ru.bankflow.transfers.dto.CreateTransferRequest;
import ru.bankflow.transfers.dto.TransferResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    public TransferResponse findById(UUID id)
    {
        TransferEntity transferEntity = repository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Перевод с id %s не найден".formatted(id))
                );

        return toResponse(transferEntity);
    }

    @Transactional
    public TransferResponse createTransfer(
            CreateTransferRequest transferToCreate,
            UUID idempotencyKey
    ) {
        if (repository.existsByIdempotencyKey(idempotencyKey)) {
            throw new IllegalArgumentException(
                    "Перевод с idempotencyKey = %s уже существует".formatted(idempotencyKey)
            );
        }

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

        var savedTransfer = repository.save(entityToSave);

        return toResponse(
                savedTransfer
        );
    }

    private TransferResponse toResponse(
            TransferEntity transferEntity
    )
    {
        return new TransferResponse(
                transferEntity.getId(),
                transferEntity.getSourceAccount().getId(),
                transferEntity.getDestinationAccount().getId(),
                transferEntity.getAmount(),
                transferEntity.getStatus(),
                transferEntity.getCreatedAt(),
                transferEntity.getIdempotencyKey()
        );
    }
}
