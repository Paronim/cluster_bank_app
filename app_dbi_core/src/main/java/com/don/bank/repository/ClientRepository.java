package com.don.bank.repository;

import com.don.bank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("FROM Client c WHERE c.phone = :phone")
    Optional<Client> findClientByPhone(long phone);
}
