package com.example.client.controllers;

import com.example.client.entites.Transaction;
import com.example.client.repositories.TransactionRepository;
import com.example.client.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private final TransactionService transactionService; // Ajout de final pour garantir l'injection
    @Autowired
    private final TransactionRepository transactionRepository;

    // ✅ Tester la connexion à la base de données
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie pour les transactions !";
    }

    // ✅ Ajouter une transaction
    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transactions) {
        return transactionRepository.save(transactions);  // Assurez-vous que le repository fonctionne
    }


    // ✅ Récupérer toutes les transactions
    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // ✅ Récupérer une transaction par ID
    @GetMapping("/{id}")
    public Optional<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionRepository.findById(id);
    }

    // ✅ Supprimer une transaction
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
        return "Transaction supprimée avec succès !";
    }

    // ✅ Mettre à jour une transaction
    @PutMapping("/updateTransaction/{id}")
    public Transaction updateTransaction(@RequestBody Transaction transactions, @PathVariable Long id) {
        return transactionService.updateTransaction(transactions);
    }
}
