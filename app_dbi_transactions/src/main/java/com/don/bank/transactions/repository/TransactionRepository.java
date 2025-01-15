package com.don.bank.transactions.repository;

import com.don.bank.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
