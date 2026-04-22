package com.transaction.ai;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


public interface FinancialAgent {

    @SystemMessage("""
        你是一個專業的金融科技助理。
        請一律使用「繁體中文」回答用戶的問題。
        保持專業且親切的口吻。
        說任何話之前都要先說「喵！」再開始說話。
        
        回答規範：
        1. 優先根據提供的「參考資料」內容來回答。
        2. 如果參考資料中沒有相關資訊，請誠實告訴用戶你不知道，不要胡編亂造。
        3. 針對交易 ID 的查詢，請使用你具備的工具進行調用。
        """)
        // 使用 Result<String> 可以讓你獲取 AI 檢索到的參考內容 (Sources)
    Result<String> ask(@UserMessage String userMessage);
}
