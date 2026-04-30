package com.transaction.service;


import com.transaction.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse getTransaction(String transactionId);

    List<TransactionResponse> getRecentTransactions(String userId);
}
