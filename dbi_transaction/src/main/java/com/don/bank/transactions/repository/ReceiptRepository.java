package com.don.bank.transactions.repository;

import com.don.bank.transactions.entity.Receipt;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends ElasticsearchRepository<Receipt, String> {

    @Query("""
            {
              "bool": {
                "must": [
                  { "match": { "accountId": "?0" } },
                  { "match": { "recipientId": "?1" } }
                ]
              }
            }
            """)
    List<Receipt> findTransactionsBySenderAndRecipientId(String accountId, String recipientId);

    @Query("""
            {
              "bool": {
                "must": [
                  { "match": { "accountId": "?0" } }
                ]
              }
            }
            """)
    List<Receipt> findTransactionsBySenderId(String accountId);
}
