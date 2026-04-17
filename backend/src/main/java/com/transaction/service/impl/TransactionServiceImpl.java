package com.transaction.service.impl;

import com.transaction.domain.entity.Transaction;
import com.transaction.domain.repository.TransactionRepository;
import com.transaction.dto.response.TransactionResponse;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository repository;


    @Override
    public TransactionResponse getTransaction(String transactionId) {
        Transaction tx = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return mapToResponse(tx);
    }


    private TransactionResponse mapToResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getTransactionId(),
                tx.getUserId(),
                tx.getAmount(),
                tx.getCurrency(),
                tx.getStatus(),
                tx.getCreatedAt()
        );
    }
}
