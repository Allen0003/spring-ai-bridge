package com.transaction.controller;

import com.transaction.ai.FinancialAgent;
import com.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiAssistantController {

    @Autowired
    FinancialAgent financialAgent;

    @Autowired
    TransactionService transactionService;

//    @GetMapping("/chat")
//    public String chat(@RequestParam String tranId, @RequestParam String msg) {
//
//        System.out.println("tranId = " + tranId);
//        System.out.println("msg = " + msg);
//
//        TransactionResponse realData = transactionService.getTransaction(tranId);
//
//        String augmentedPrompt = String.format(
//                "這是從後台系統查詢到的真實數據：%s\n\n用戶的問題是：%s",
//                realData.toString(),
//                msg
//        );
//
//        System.out.println("msg = " + augmentedPrompt);
//
//        return financialAgent.ask(augmentedPrompt);
//    }


    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        System.out.println("AI 正在嘗試呼叫工具，參數: " + msg);

        // 範例輸入： "幫我查交易 TX123 的狀態，並解釋給我聽"
        // 你不再需要手動查 TransactionResponse，AI 會自己去執行 Tool

        String res = financialAgent.ask(msg);

        System.out.println(" res = " + res);

        return res;
    }


}
