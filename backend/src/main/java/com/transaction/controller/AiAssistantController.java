package com.transaction.controller;

import com.transaction.ai.FinancialAgent;
import com.transaction.service.KnowledgeBaseService;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
        System.out.println("AI 正在嘗試呼叫工具，參數: " + msg);

        dev.langchain4j.service.Result<String> aiResult = financialAgent.ask("chat-00", msg);
        Map<String, Object> response = new HashMap<>();
        response.put("answer", aiResult.content());

        // 將 sources 從物件清單轉成單純的 String 清單
        List<String> sourceTexts = aiResult.sources().stream()
                .map(content -> content.textSegment().text())
                .toList();

        response.put("sources", sourceTexts);

        System.out.println(response);
        return response;
    }


}
