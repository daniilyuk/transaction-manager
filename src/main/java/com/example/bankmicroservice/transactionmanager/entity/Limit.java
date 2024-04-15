package com.example.bankmicroservice.transactionmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "limits")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "limit_amount")
    private BigDecimal limitAmount;

    @Column(name = "limit_balance")
    private BigDecimal limitBalance;

    @Column(name = "limit_datetime")
    private LocalDateTime limitDatetime;

    @Column(name = "currency_short_name")
    private String currencyShortName;

    @Column(name = "category")
    private String category;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "is_active")
    private Boolean isActive;
}

