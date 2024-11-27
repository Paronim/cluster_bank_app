package com.don.bank.repository;

import com.don.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("FROM Account a WHERE a.client.id = :client_id")
    List<Account> findByClientId(@Param("client_id") long clientId);

}
