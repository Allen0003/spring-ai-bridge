package com.transaction.ai.config;

import com.transaction.ai.FinancialAgent;
import com.transaction.ai.TransactionTools;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;

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
                .topK(1)
                .maxOutputTokens(500)
                .build();
    }

    //將 Agent 與 Tool 綁定
    @Bean
    public FinancialAgent financialAgent(
            ChatLanguageModel model,
            TransactionTools tools,
            ContentRetriever contentRetriever,  // <--- 1. 注入你定義好的檢索器
            ChatMemoryProvider chatMemoryProvider // <--- 1. 注入記憶供應器
    ) {
        return AiServices.builder(FinancialAgent.class)
                .chatLanguageModel(model)
                .tools(tools)
                .contentRetriever(contentRetriever) // <--- 2. 這裡就是把「法規知識庫」丟給 AI 的地方
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }


    @Bean
    public EmbeddingModel embeddingModel() {
        return GoogleAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-embedding-001")
                .build();
    }


    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5432)
                .database("financial_db")
                .user("admin")
                .password("password123")
                .table("financial_knowledge")
                .dimension(3072) // Gemini 向量固定是 768 維
                .build();
    }


    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> store, EmbeddingModel model) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(model)
                .maxResults(2) // 每次搜尋回傳最相關的前 3 段
                .minScore(0.7) // 相似度太低（低於 0.7）的資料不要，避免 AI 亂編
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(5);
    }

}
