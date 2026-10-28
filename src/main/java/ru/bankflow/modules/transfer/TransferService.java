package ru.bankflow.modules.transfer;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bankflow.modules.account.AccountEntity;
import ru.bankflow.modules.account.AccountRepository;
import ru.bankflow.exceptions.IdempotencyKeyConflictException;
import ru.bankflow.modules.account.BalanceService;
import ru.bankflow.modules.ledger.EntryService;
import ru.bankflow.modules.ledger.OperationType;
import ru.bankflow.modules.transfer.dto.CreateTransferRequest;
import ru.bankflow.modules.transfer.dto.TransferResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    private final BalanceService balanceService;

    private final EntryService entryService;

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

        var optionalExistedRepository = repository.findByIdempotencyKey(idempotencyKey);

        if (optionalExistedRepository.isPresent()) {

            var existedTransfer = optionalExistedRepository.get();

            if (
                    transferToCreate.sourceAccount().equals(existedTransfer.getSourceAccount().getId())
                            && transferToCreate.destinationAccount().equals(existedTransfer.getDestinationAccount().getId())
                            && transferToCreate.amount().equals(existedTransfer.getAmount()))
            {
                return toResponse(existedTransfer);
            }

            throw new IdempotencyKeyConflictException(
                    "Перевод с idempotencyKey = %s уже существует".formatted(idempotencyKey)
            );
        }

        //Реализована блокировка счётов по возрастанию их UUID для избежания взаимной блокировки
        UUID firstId;
        UUID secondId;

        if (transferToCreate.sourceAccount().compareTo(transferToCreate.destinationAccount()) < 0)
        {
            firstId = transferToCreate.sourceAccount();
            secondId = transferToCreate.destinationAccount();
        } else {
            firstId = transferToCreate.destinationAccount();
            secondId = transferToCreate.sourceAccount();
        }

        AccountEntity firstAccount = accountRepository.findAccountForUpdate(firstId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Счёт отправителя с id %s не найден".formatted(transferToCreate.sourceAccount()
                        )));

        AccountEntity secondAccount = accountRepository.findAccountForUpdate(secondId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Счёт получателя с id %s не найден".formatted(transferToCreate.destinationAccount()
                        )));

        AccountEntity sourceAccount = transferToCreate.sourceAccount().equals(firstId)
                ? firstAccount : secondAccount;

        AccountEntity destinationAccount = transferToCreate.destinationAccount().equals(firstId)
                ? firstAccount : secondAccount;

        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new IllegalArgumentException("Счёт отправителя и получателя должны различаться!");
        }

        balanceService.processDebit(sourceAccount, transferToCreate.amount());
        balanceService.processCredit(destinationAccount, transferToCreate.amount());

        var savedSourceAccount = accountRepository.save(sourceAccount);
        var savedDestinationAccount = accountRepository.save(destinationAccount);

        TransferEntity entityToSave = new TransferEntity(
                savedSourceAccount,
                savedDestinationAccount,
                transferToCreate.amount(),
                idempotencyKey
        );

        var savedTransfer = repository.save(entityToSave);

        entryService.create(
                savedSourceAccount,
                transferToCreate.amount().negate(),
                OperationType.DEBIT,
                savedTransfer,
                UUID.randomUUID()
        );

        entryService.create(
                savedDestinationAccount,
                transferToCreate.amount(),
                OperationType.CREDIT,
                savedTransfer,
                UUID.randomUUID()
        );

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
