package com.example.bankmicroservice.transactionmanager.util;

public enum CurrencyShortName {
    USD("USD"),
    RUB("RUB"),
    KZT("KZT");
    private final String name;

    CurrencyShortName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
