package com.transaction.ai;

import com.transaction.dto.response.TransactionResponse;
import com.transaction.service.TransactionService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.langchain4j.agent.tool.P;

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

    @Tool("獲取指定用戶最近 24 小時的消費頻率與地點統計, 如果沒提供id 就用 uid = 10")
    public String getTransactionStatistics(@P("用戶ID") String userId) {
        // 模擬 SQL: SELECT count(*), group_concat(location), sum(amount) ...
        // 實作建議：回傳 JSON 字串讓 Gemini 解析
        return """
            [
                {"id": 1, "uid": 10, "amount": 100, "location": "台北", "time": "2026-04-30 10:00"},
                {"id": 2, "uid": 10, "amount": 50000, "location": "倫敦", "time": "2026-04-30 10:05"},
                {"id": 3, "uid": 82, "amount": 100, "location": "台北", "time": "2026-04-30 10:10"},
                {"id": 4, "uid": 10, "amount": 100, "location": "紐約", "time": "2026-04-30 10:19"}
                
            ]
            """;
    }

}
