package com.don.bank.repository;

import com.don.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t WHERE t.account.id = :accountId AND t.recipient.id = :recipientId")
    List<Transaction> findByAccountAndRecipient(@Param("accountId") Long accountId, @Param("recipientId") Long recipientId);
}
