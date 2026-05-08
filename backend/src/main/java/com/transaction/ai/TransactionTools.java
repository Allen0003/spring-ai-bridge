package com.transaction.ai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.transaction.domain.entity.UserEntity;
import com.transaction.dto.response.TransactionResponse;
import com.transaction.service.TransactionService;
import com.transaction.service.UserService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionTools {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.format(DATE_FORMATTER)))
            .create();

    // 定義統一的時間格式
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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

    @Tool("獲取指定用戶最近 24 小時的消費統計，包含金額與交易地點")
    public String getTransactionStatistics(@P("用戶ID") String userId) {
        List<TransactionResponse> txs = transactionService.getRecentTransactions(userId);
        if (txs.isEmpty()) return "查無交易紀錄。";

        return gson.toJson(txs);
    }


    @Tool("根據用戶 ID 鎖定其銀行帳戶與信用卡，防止進一步損失")
    public String lockUserAccount(@P("要鎖定的用戶 ID") String userId) {
        // 呼叫你的 Service 執行 SQL: UPDATE users SET status = 'LOCKED' WHERE user_id = ...
        UserEntity user = userService.lockAccount(userId);

        if (user.getStatus() == 0) {
            return "成功！用戶 " + userId + " 的帳戶已暫時凍結，喵！";
        } else {
            return "錯誤：無法鎖定用戶 " + userId + "，請聯繫系統管理員。";
        }
    }


}
