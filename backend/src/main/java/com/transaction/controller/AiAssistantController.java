package com.transaction.controller;

import com.transaction.ai.FinancialAgent;
import com.transaction.service.KnowledgeBaseService;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiAssistantController {

    @Autowired
    FinancialAgent financialAgent;

    @Autowired
    KnowledgeBaseService knowledgeBaseService;


    @GetMapping("/initData")
    public void initData() {
        knowledgeBaseService.importFinancialRules();
    }

    @GetMapping("/testRAG")
    public void testRag() {
        knowledgeBaseService.testSearch();
    }

    @GetMapping("/chat")
    public Map<String, Object> chat(@RequestParam String msg) {
        System.out.println("喵探員收到指令: " + msg);

        Map<String, Object> response = new HashMap<>();

        String currentMemoryId = "user-10";
        if (msg.contains("鎖")) {
            // 給它一個全新的 memoryId，確保對話轉折是乾淨的 User -> Tool
            currentMemoryId = "action-" + System.currentTimeMillis();
            // 為了讓它知道要鎖誰，我們手動把 ID 塞進去
            msg = "我是用戶 10，請立刻執行鎖定帳戶工具，喵！";
        }

        try {
            // 注意：如果你的介面定義是 String chat(String msg)，直接收 String
            // 如果是 Result<String>，才需要 content()
            String answer = financialAgent.chat(currentMemoryId, msg);

            response.put("answer", answer);
//            response.put("sources", new ArrayList<>()); // 目前純分析，無來源

        } catch (Exception e) {
            e.printStackTrace();
            response.put("answer", "喵... 系統出錯了，人類快來修好我！");
        }
        return response;
    }


}
