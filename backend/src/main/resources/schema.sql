CREATE TABLE financial_knowledge (
    embedding_id uuid PRIMARY KEY DEFAULT gen_random_uuid(), -- 它預設找 embedding_id
    text text,                                     -- 它預設找 text
    metadata jsonb,                                       -- 它預設找 metadata
    embedding vector(3072)                                 -- 你的維度
)
