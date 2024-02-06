package com.example.concurrency.service;

import com.example.concurrency.domain.StockOptimistic;
import com.example.concurrency.repository.StockOptimisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockStockService {

    private final StockOptimisticRepository stockOptimisticRepository;

    public OptimisticLockStockService(StockOptimisticRepository stockOptimisticRepository) {
        this.stockOptimisticRepository = stockOptimisticRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        StockOptimistic stock = stockOptimisticRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);
        stockOptimisticRepository.save(stock);
    }
}
