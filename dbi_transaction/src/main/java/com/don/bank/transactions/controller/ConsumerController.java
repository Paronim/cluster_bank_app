package com.don.bank.transactions.controller;

import com.don.bank.
import com.don.bank.transactions.service.TransactionService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ConsumerController {

    private final TransactionService transactionService;
    Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    public ConsumerController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = "transactionQueue")
    public void receiveMessage(@Payload ReceiptDTO receiptDTO, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            transactionService.save(receiptDTO);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }


}
