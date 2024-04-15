package com.example.bankmicroservice.transactionmanager.util;

public enum ExpenseCategory {
    PRODUCT("product"),
    SERVICE("service");
    private final String name;
    ExpenseCategory(String name) {
        this.name = name;
    }
}
