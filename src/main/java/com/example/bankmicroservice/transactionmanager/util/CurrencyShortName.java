package com.example.bankmicroservice.transactionmanager.util;

import lombok.Getter;

@Getter
public enum CurrencyShortName {
    USD("USD"),
    RUB("RUB"),
    KZT("KZT");
    private final String name;

    CurrencyShortName(String name) {
        this.name = name;
    }

}
