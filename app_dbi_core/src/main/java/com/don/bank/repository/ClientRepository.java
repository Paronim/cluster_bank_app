package com.don.bank.repository;

import com.don.bank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("FROM Client c WHERE c.phone = :phone AND c.status = 'active'")
    Optional<Client> findClientByPhone(long phone);
}
