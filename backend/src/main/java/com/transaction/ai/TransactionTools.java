package com.transaction.ai;

import com.transaction.dto.response.TransactionResponse;
import com.transaction.service.TransactionService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionTools {

    @Autowired
    TransactionService transactionService;

    //tool 寫出來是給 AI 看的
    @Tool("根據交易 ID (transactionId) 查詢詳細資訊")
    public String getTransactionDetails(String transactionId) {

        TransactionResponse tx = transactionService.getTransaction(transactionId);

        if (tx == null) return "找不到 ID 為 " + transactionId + " 的交易。";
        return String.format(
                "找到交易！ID: %s, 用戶: %s, 金額: %f %s, 狀態: %s, 時間: %s",
                tx.getTransactionId(), tx.getUserId(), tx.getAmount(),
                tx.getCurrency(), tx.getStatus(), tx.getCreatedAt()
        );
    }
}
