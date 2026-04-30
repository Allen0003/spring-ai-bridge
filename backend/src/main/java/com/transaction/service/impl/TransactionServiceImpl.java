package com.transaction.service.impl;

import com.transaction.domain.entity.Transaction;
import com.transaction.domain.repository.TransactionRepository;
import com.transaction.dto.response.TransactionResponse;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<TransactionResponse> getRecentTransactions(String userId) {
        // 實務上可以下 SQL 限制 24 小時內，這裡先實作獲取該用戶所有交易供 AI 分析
        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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
