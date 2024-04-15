package com.example.bankmicroservice.transactionmanager.repository;

import com.example.bankmicroservice.transactionmanager.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(Long accountNumber);
}
