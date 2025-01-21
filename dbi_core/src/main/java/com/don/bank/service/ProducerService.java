package com.don.bank.service;

import com.don.common.models.TransactionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private RabbitTemplate rabbitTemplate;

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(TransactionDTO transactionDTO) {
        rabbitTemplate.convertAndSend("transactionExchange", "transactions", transactionDTO);
    }

}
