package com.example.bankmicroservice.transactionmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_from", referencedColumnName="id")
    private Account accountFrom;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_to", referencedColumnName="id")
    private Account accountTo;

    @Column(name = "currency_shortname")
    private String currencyShortName;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "expense_category")
    private String expenseCategory;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "limit_exceeded")
    private boolean limitExceeded;
}
