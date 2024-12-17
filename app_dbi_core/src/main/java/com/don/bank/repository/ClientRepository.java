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

    @Query("FROM Client c WHERE c.phone = :phone")
    Optional<Client> findClientByPhone(long phone);

    @Transactional
    @Modifying
    @Query("UPDATE Client c SET " +
            "c.firstName = CASE WHEN :#{#client.firstName} IS NOT NULL THEN :#{#client.firstName} ELSE c.firstName END, " +
            "c.lastName = CASE WHEN :#{#client.lastName} IS NOT NULL THEN :#{#client.lastName} ELSE c.lastName END, " +
            "c.phone = CASE WHEN :#{#client.phone} IS NOT NULL THEN :#{#client.phone} ELSE c.phone END, " +
            "c.password = CASE WHEN :#{#client.password} IS NOT NULL THEN :#{#client.password} ELSE c.password END " +
            "WHERE c.id = :#{#client.id}")
    void updateClient(@Param("client") Client client);
}
