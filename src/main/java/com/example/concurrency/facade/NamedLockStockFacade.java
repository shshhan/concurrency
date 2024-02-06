package com.example.concurrency.facade;

import com.example.concurrency.service.NamedLockStockService;
import com.example.concurrency.repository.NamedLockRepository;
import org.springframework.stereotype.Component;

@Component
public class NamedLockStockFacade {

    private final NamedLockRepository namedLockRepository;

    private final NamedLockStockService namedLockStockService;

    public NamedLockStockFacade(NamedLockRepository namedLockRepository, NamedLockStockService namedLockStockService) {
        this.namedLockRepository = namedLockRepository;
        this.namedLockStockService = namedLockStockService;
    }

    public void decrease(Long id, Long quantity) {
        try {
            namedLockRepository.getLock(id.toString());
            namedLockStockService.decrease(id, quantity);
        } finally {
            namedLockRepository.releaseLock(id.toString());
        }
    }
}
