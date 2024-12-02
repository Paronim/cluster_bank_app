package com.don.bank.service;

import com.don.bank.dto.TransactionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
