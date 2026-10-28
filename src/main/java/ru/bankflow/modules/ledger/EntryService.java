package ru.bankflow.modules.ledger;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bankflow.exceptions.IdempotencyKeyConflictException;
import ru.bankflow.modules.account.AccountEntity;
import ru.bankflow.modules.ledger.dto.EntryResponse;
import ru.bankflow.modules.transfer.TransferEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;

    public void create(
            AccountEntity account,
            BigDecimal flow_funds,
            OperationType operationType,
            TransferEntity transfer,
            UUID idempotencyKey
    ) {
        Optional<EntryEntity> existedEntryOptional = entryRepository.findByIdempotencyKey(idempotencyKey);

        if (existedEntryOptional.isPresent()) {
            var existedEntry = existedEntryOptional.get();

            if (existedEntry.getFlowFunds().equals(flow_funds)
                && existedEntry.getOperationType().equals(operationType)) {
                return;
            }
            else {
                throw new IdempotencyKeyConflictException("Запись в журнале с idempotency key %s уже существует".formatted(idempotencyKey));
            }
        }

        EntryEntity entryEntity;

        if (transfer == null) {
            entryEntity = new EntryEntity(
                    account,
                    flow_funds,
                    operationType,
                    idempotencyKey
            );
        } else {
            entryEntity = new EntryEntity(
                    account,
                    flow_funds,
                    operationType,
                    transfer,
                    idempotencyKey
            );
        }
        entryRepository.save(entryEntity);
    }

    public EntryResponse findById(UUID id) {

        EntryEntity entryEntity = entryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись с id %s не найдена".formatted(id)));

        return toResponse(entryEntity);
    }

    public List<EntryResponse> findEntriesByAccount(UUID accountId, Integer pageSize, Integer pageNumber) {

        int validPageSize = pageSize != null
                ? pageSize : 20;
        int validPageNumber = pageNumber != null
                ? pageNumber : 0;

        Pageable pageable = Pageable.ofSize(validPageSize).withPage(validPageNumber);

        List<EntryEntity> entryEntities = entryRepository.findByAccountId(accountId, pageable)
                .orElseThrow(() -> new EntityNotFoundException("История переводов для аккаунта с id %s не найдена".formatted(accountId)));

        return entryEntities.stream()
                .map(this::toResponse)
                .toList();
    }

    private EntryResponse toResponse(EntryEntity entryEntity) {
        return new EntryResponse(
                entryEntity.getId(),
                entryEntity.getAccount().getId(),
                entryEntity.getFlowFunds(),
                entryEntity.getOperationType(),
                entryEntity.getTransfer().getId(),
                entryEntity.getCreatedAt()
        );
    }
}
