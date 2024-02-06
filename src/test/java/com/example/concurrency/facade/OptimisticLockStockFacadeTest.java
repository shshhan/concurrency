package com.example.concurrency.facade;

import com.example.concurrency.domain.StockOptimistic;
import com.example.concurrency.repository.StockOptimisticRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OptimisticLockStockFacadeTest {

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;
    @Autowired
    private StockOptimisticRepository stockOptimisticRepository;

    @BeforeEach
    void setUp() {
        stockOptimisticRepository.saveAndFlush(new StockOptimistic(1L, 100L));
    }

    @AfterEach
    void tearDown() {
        stockOptimisticRepository.deleteAll();
    }

    @Test
    void 동시_100개_재고감소_요청_optimistic_lock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        StockOptimistic stockOptimistic = stockOptimisticRepository.findById(1L).orElseThrow();
        assertEquals(0, stockOptimistic.getQuantity());
    }
}