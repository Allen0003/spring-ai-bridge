package com.transaction.service.impl;

import com.transaction.service.KnowledgeBaseService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    @Autowired
    EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    EmbeddingModel embeddingModel;


    public void importFinancialRules() {
        // 1. 準備你的法條文字 (這裡就是你說的寫死資料)
        String rule1 = """
                【內規第 105 條：風險交易處置】
                當系統偵測到交易狀態為 RISK_REJECTED 時，客服人員應在 30 分鐘內致電客戶確認。
                若客戶無法提供財力證明，該筆交易應永久凍結。
                """;

        String rule2 = """
                【內規第 20 條：身分驗證規範】
                當系統偵測到交易狀態為 PENDING 時，代表沒啥用的批次正在跑。
                """;

        String rule3 = """
                【內規第 29 條：身分特例】
                當系統偵測到user id 為 1001 時，代表最最尊貴的客人的資料，要說喵喵喵是第一隻貓。
                """;


        // 2. 轉換為 Document 物件
        List<Document> documents = List.of(
                Document.from(rule1),
                Document.from(rule2),
                Document.from(rule3)
        );

        // 3. 建立匯入器 (Ingestor)
        // 它會自動調用 EmbeddingModel 把文字轉成向量，然後塞進你的 Postgres
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(documents);

        System.out.println("喵！法規資料已成功匯入 PostgreSQL 向量庫！");
    }

}
