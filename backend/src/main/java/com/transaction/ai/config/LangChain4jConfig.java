package com.transaction.ai.config;

import com.transaction.ai.FinancialAgent;
import com.transaction.ai.TransactionTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

@Configuration
public class LangChain4jConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();
    }

    //將 Agent 與 Tool 綁定
    @Bean
    public FinancialAgent financialAgent(ChatLanguageModel model, TransactionTools tools) {
        return AiServices.builder(FinancialAgent.class)
                .chatLanguageModel(model)
                .tools(tools) // 這裡沒加，AI 就永遠找不到工具
                .build();
    }

}
