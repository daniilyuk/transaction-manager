package com.example.bankmicroservice.transactionmanager.repository;

import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
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

    @Query("""
            SELECT NEW com.example.bankmicroservice.transactionmanager.dto.TransactionResponse(
                t.accountFrom.id,
                t.accountTo.id,
                t.currencyShortName,
                t.sum,
                t.expenseCategory,
                t.datetime,
                l.limitAmount,
                l.limitDatetime,
                l.currencyShortName
            )
            FROM Transaction t
            JOIN Limit l ON t.accountFrom.id = l.account.id
            WHERE t.limitExceeded = true AND t.accountFrom = :accountFrom""")
    List<TransactionResponse> findTransactionsWithLimitExceededAndNearestLimit(@Param("accountFrom") Long accountFrom);


    List<Transaction> findTransactionByAccountToAccountNumber(Long accountNumber);
}
