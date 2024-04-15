package com.example.bankmicroservice.transactionmanager.repository;

import com.example.bankmicroservice.transactionmanager.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findCurrencyBySymbolLike(String symbol);
}
