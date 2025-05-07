package com.example.client.services;

import com.example.client.entites.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction addTransaction(Transaction transaction);
    Transaction deleteTransaction(long id);
    List<Transaction> getAllTransactions();
    Transaction updateTransaction(Transaction transaction);
}
