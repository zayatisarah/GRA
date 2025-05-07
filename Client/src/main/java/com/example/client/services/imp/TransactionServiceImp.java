package com.example.client.services.imp;

import com.example.client.entites.Transaction;
import com.example.client.repositories.TransactionRepository;
import com.example.client.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImp(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction deleteTransaction(long id) {
        transactionRepository.deleteById(id);
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
