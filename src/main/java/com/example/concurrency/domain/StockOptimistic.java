package com.example.concurrency.domain;

import jakarta.persistence.*;

@Entity
public class StockOptimistic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long quantity;
    @Version
    private Long version;


    public StockOptimistic() {
    }

    public StockOptimistic(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        if(this.quantity - quantity < 0) {
            throw new RuntimeException("재고는 0미만이 될 수 없습니다.");
        }
        this.quantity -= quantity;
    }
}
