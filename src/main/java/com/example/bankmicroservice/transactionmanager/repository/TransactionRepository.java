package com.example.bankmicroservice.transactionmanager.repository;

import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Modifying
    @Query("update Transaction t set t.limitExceeded = true where t.id = :transaction_id")
    void updateTransactionById(@Param("transaction_id") Long id);

    @Query("SELECT NEW com.example.bankmicroservice.transactionmanager.dto.TransactionResponse(\n" +
            "    t.accountFrom.id,\n" +
            "    t.accountTo.id,\n" +
            "    t.currencyShortName,\n" +
            "    t.sum,\n" +
            "    t.expenseCategory,\n" +
            "    t.datetime,\n" +
            "    l.limitAmount,\n" +
            "    l.limitDatetime,\n" +
            "    l.currencyShortName\n" +
            ")\n" +
            "FROM Transaction t\n" +
            "JOIN Limit l ON t.accountFrom.id = l.account.id\n" +
            "WHERE t.limitExceeded = true")
    List<TransactionResponse> findTransactionsWithLimitExceededAndNearestLimit();


    List<Transaction> findTransactionByAccountTo_AccountNumber(Long accountNumber);
}
