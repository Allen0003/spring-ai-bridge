package com.transaction.ai.config;

import com.transaction.ai.FinancialAgent;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.transaction.ai.TransactionTools;

import java.time.Duration;

@Configuration
public class LangChain4jConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434") // Ollama 預設埠
                .modelName("llama3.2") // 改用 llama3.2
                .temperature(0.0)                  // 金融專案建議低溫度，回覆更穩定
                .timeout(Duration.ofSeconds(125))   // Local 跑模型可能較慢，超時設長一點
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
