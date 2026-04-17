package com.transaction.service;


import com.transaction.dto.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse getTransaction(String transactionId);
}
