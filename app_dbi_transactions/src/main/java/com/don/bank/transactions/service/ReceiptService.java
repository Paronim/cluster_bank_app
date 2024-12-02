package com.don.bank.transactions.service;

import com.don.bank.transactions.dto.ReceiptDTO;
import com.don.bank.transactions.entity.Receipt;
import com.don.bank.transactions.repository.ReceiptRepository;
import com.don.bank.transactions.utils.mappingUtils.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing receipt-related operations in the banking system.
 * This service handles operations such as saving receipts, retrieving receipts by different criteria,
 * and performing batch operations for receipts.
 */
@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    /**
     * Constructs the {@link ReceiptService} with the necessary dependencies.
     *
     * @param receiptRepository the repository responsible for managing receipt data
     */
    private ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    /**
     * Saves a single receipt to the database.
     * The receipt is mapped from the provided {@link ReceiptDTO} object before saving.
     *
     * @param receiptDTO the receipt data transfer object to be saved
     */
    public void save(ReceiptDTO receiptDTO) {
        receiptRepository.save(MappingUtils.mapToReceipt(receiptDTO));
    }

    /**
     * Saves a list of receipts to the database. This method first deletes the existing receipts
     * based on the provided list and then saves the new receipts.
     *
     * @param receiptDTOList the list of receipt data transfer objects to be saved
     */
    @Transactional
    public void saveAll(List<ReceiptDTO> receiptDTOList) {
        List<Receipt> receiptList = receiptDTOList.stream()
                .map(MappingUtils::mapToReceipt)
                .toList();

        receiptRepository.deleteAll(receiptList);
        receiptRepository.saveAll(receiptList);
    }

    /**
     * Retrieves all receipts from the database.
     *
     * @return a list of {@link ReceiptDTO} representing all receipts
     */
    public List<ReceiptDTO> getAllReceipt() {
        Iterable<Receipt> iterable = receiptRepository.findAll();
        List<ReceiptDTO> receiptDTOList = new ArrayList<>();
        iterable.forEach(receipt -> receiptDTOList.add(MappingUtils.mapToReceiptDTO(receipt)));
        return receiptDTOList;
    }

    /**
     * Retrieves a receipt by its unique identifier.
     *
     * @param id the unique identifier of the receipt
     * @return a {@link ReceiptDTO} representing the receipt, or null if not found
     */
    public ReceiptDTO getReceiptById(long id) {
        Receipt receipt = receiptRepository.findById(String.valueOf(id)).orElse(null);
        return receipt != null ? MappingUtils.mapToReceiptDTO(receipt) : null;
    }

    /**
     * Retrieves receipts based on the sender and optionally the recipient account IDs.
     * If the recipient ID is not provided, only the sender's receipts are returned.
     * If the recipient ID is provided, receipts matching both sender and recipient are returned.
     *
     * @param accountId the ID of the sender's account
     * @param recipientId the ID of the recipient's account, may be null
     * @return a list of {@link ReceiptDTO} matching the criteria
     */
    public List<ReceiptDTO> getReceiptBySenderAndRecipientId(long accountId, Long recipientId) {
        Iterable<Receipt> iterable;
        if (recipientId == null) {
            iterable = receiptRepository.findTransactionsBySenderId(String.valueOf(accountId));
        } else {
            iterable = receiptRepository.findTransactionsBySenderAndRecipientId(String.valueOf(accountId), String.valueOf(recipientId));
        }

        List<ReceiptDTO> receiptDTOList = new ArrayList<>();
        iterable.forEach(receipt -> receiptDTOList.add(MappingUtils.mapToReceiptDTO(receipt)));
        return receiptDTOList;
    }

}
