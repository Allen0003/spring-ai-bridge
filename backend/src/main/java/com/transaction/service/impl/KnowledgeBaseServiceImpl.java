package com.transaction.service.impl;

import com.transaction.service.KnowledgeBaseService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
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

    @Autowired
    ContentRetriever contentRetriever;

    public void testSearch() {
        // 模擬使用者問問題
        Query query = Query.from("如果看到客戶1001要怎麼辦？");

        // 檢索器會去 Postgres 找最相關的片段
        List<Content> results = contentRetriever.retrieve(query);

        results.forEach(content -> {
            System.out.println("找出的相關法條內容: " + content.textSegment().text());
        });
    }


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

        String rule4 = """
        【內規第 45 條：外幣交易限制】
        所有以 USD (美金) 進行的交易，若狀態長時間維持在 PENDING，代表觸發了跨境匯款人工審核流程，預計耗時 1 至 3 個工作天。
        """;

        String rule5 = """
        【內規第 88 條：重複請求定義】
        若系統收到相同的 idempotency_key，應直接回傳快取中的舊有結果，不得重新執行帳務扣款，以避免重複交易。
        """;

        String rule6 = """
        【內規第 12 條：風險等級標記】
        若用戶在 24 小時內出現超過兩次狀態為 RISK_REJECTED 的紀錄，該用戶 ID 應被標記為「高風險觀察名單」。
        """;

        String rule7 = """
        【內規第 99 條：喵星人專屬福利】
        身分特例為 1001 的最尊貴客人，其所有手續費應全額減免，且在系統紀錄中應標註為「第一隻貓」。
        """;

        String rule8 = """
        【內規第 30 條：高額交易授權】
        單筆交易金額超過 1000 TWD 且狀態為 SUCCESS 時，系統應自動發送 Push 通知與 Email 憑證給客戶。
        """;


        // 2. 轉換為 Document 物件
        List<Document> documents = List.of(
                Document.from(rule1),
                Document.from(rule2),
                Document.from(rule3),
                Document.from(rule4),
                Document.from(rule5),
                Document.from(rule6),
                Document.from(rule7),
                Document.from(rule8)
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
