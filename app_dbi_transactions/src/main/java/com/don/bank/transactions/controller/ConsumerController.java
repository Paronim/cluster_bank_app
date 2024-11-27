package com.don.bank.transactions.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @RabbitListener(queues = "transactionsQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
