package com.example.concurrency.repository;

import com.example.concurrency.domain.StockOptimistic;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface StockOptimisticRepository extends JpaRepository<StockOptimistic, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from StockOptimistic s where s.id = :id")
    StockOptimistic findByIdWithOptimisticLock(Long id);
}
