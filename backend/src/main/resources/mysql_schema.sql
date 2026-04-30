-- 刪除舊表
DROP TABLE IF EXISTS `transactions`;
DROP TABLE IF EXISTS `users`;

-- 重新建立 User 表 (配合你之前的 Entity)
CREATE TABLE `users` (
  `uid` VARCHAR(50) NOT NULL PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150),
  `phone` VARCHAR(20),
  `status` TINYINT(1) DEFAULT '1', -- 1: 正常, 0: 鎖定
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 重新建立 Transaction 表，加入 location 欄位
CREATE TABLE `transactions` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `transaction_id` VARCHAR(50) NOT NULL UNIQUE,
  `user_id` VARCHAR(50) NOT NULL,
  `amount` DECIMAL(18, 4) NOT NULL,
  `currency` VARCHAR(10) DEFAULT 'TWD',
  `location` VARCHAR(100) NOT NULL COMMENT '交易地區',
  `status` VARCHAR(20) NOT NULL,
  `idempotency_key` VARCHAR(100) UNIQUE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;