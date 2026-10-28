package ru.bankflow.modules.ledger;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankflow.modules.account.AccountEntity;
import ru.bankflow.modules.ledger.dto.EntryResponse;
import ru.bankflow.modules.transfer.TransferEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;

    public void create(
            AccountEntity account,
            BigDecimal flow_funds,
            OperationType operationType,
            TransferEntity transfer
    ) {
        EntryEntity entryEntity;

        if (transfer == null) {
            entryEntity = new EntryEntity(
                    account,
                    flow_funds,
                    operationType
            );
        } else {
            entryEntity = new EntryEntity(
                    account,
                    flow_funds,
                    operationType,
                    transfer
            );
        }
        entryRepository.save(entryEntity);
    }

    public EntryResponse findById(UUID id) {

        EntryEntity entryEntity = entryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись с id %s не найдена".formatted(id)));

        return toResponse(entryEntity);
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
