package com.example.bankmicroservice.transactionmanager.repository;

import com.example.bankmicroservice.transactionmanager.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LimitRepository extends JpaRepository<Limit,Long> {
    @Query("SELECT l FROM Limit l WHERE l.account.id = :accountId AND l.category = :category AND l.isActive = true")
    Limit findLimitByAccountAndCategoryAndIsActiveIsTrue(@Param("accountId") Long accountId, @Param("category") String category);

    @Modifying
    @Query("update Limit l set l.limitBalance = :newLimitBalance where l.id =:id")
    void updateLimitById(@Param("id") Long id, @Param("newLimitBalance") BigDecimal newLimitBalance);

    @Query("SELECT l FROM Limit l WHERE l.account.accountNumber=:accountNumber")
    List<Limit> findLimitsByAccount(@Param("accountNumber") Long accountNumber);
}
