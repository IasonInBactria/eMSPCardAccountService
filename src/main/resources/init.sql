-- accounts表
CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          status VARCHAR(50) NOT NULL, -- 'CREATED', 'ACTIVATED', 'DEACTIVATED'
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- cards表
CREATE TABLE cards (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       contract_id VARCHAR(255) UNIQUE NOT NULL,
                       rfid_uid VARCHAR(255) UNIQUE NOT NULL,
                       rfid_visible_number VARCHAR(255) NOT NULL,
                       status VARCHAR(50) NOT NULL, -- 'CREATED', 'ASSIGNED', 'ACTIVATED', 'DEACTIVATED'
                       account_id BIGINT, -- 外键，指向 accounts 表的 BIGINT ID
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       CONSTRAINT fk_account
                           FOREIGN KEY (account_id)
                               REFERENCES accounts (id)
                               ON DELETE SET NULL -- 如果账户被删除，卡片上的 account_id 设为 NULL
);

CREATE INDEX idx_accounts_last_updated ON accounts (last_updated);
CREATE INDEX idx_cards_last_updated ON cards (last_updated);
CREATE INDEX idx_cards_account_id ON cards (account_id);
