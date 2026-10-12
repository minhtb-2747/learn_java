-- Schema khởi tạo: users, social_accounts, posts, social_metrics
-- Thời gian lưu UTC, dùng DATETIME(6) vì TIMESTAMP giới hạn năm 2038

-- Admin đăng nhập dashboard qua OAuth2 (Facebook / Twitter)
CREATE TABLE users (
	id               BIGINT       NOT NULL AUTO_INCREMENT,
	-- Twitter mặc định không trả email nên cho phép NULL
	email            VARCHAR(255) NULL,
	name             VARCHAR(100) NOT NULL,
	provider         VARCHAR(20)  NOT NULL,
	provider_id      VARCHAR(100) NOT NULL,
	-- Token không cố định độ dài, có thể dài hơn 255
	access_token     TEXT         NOT NULL,
	token_expires_at DATETIME(6)  NULL,
	created_at       DATETIME(6)  NOT NULL,
	updated_at       DATETIME(6)  NOT NULL,
	CONSTRAINT pk_users PRIMARY KEY (id),
	CONSTRAINT uk_users_email UNIQUE (email),
	CONSTRAINT uk_users_provider_provider_id UNIQUE (provider, provider_id),
	CONSTRAINT ck_users_provider CHECK (provider IN ('FACEBOOK', 'TWITTER'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Trang Facebook / tài khoản Twitter đang theo dõi
CREATE TABLE social_accounts (
	id                   BIGINT       NOT NULL AUTO_INCREMENT,
	platform             VARCHAR(20)  NOT NULL,
	platform_account_id  VARCHAR(100) NOT NULL,
	name                 VARCHAR(255) NOT NULL,
	account_url          VARCHAR(500) NULL,
	-- followers là chỉ số của tài khoản, không phải của bài viết
	followers            BIGINT       NOT NULL DEFAULT 0,
	-- NULL = chưa crawl lần nào
	followers_updated_at DATETIME(6)  NULL,
	created_at           DATETIME(6)  NOT NULL,
	updated_at           DATETIME(6)  NOT NULL,
	CONSTRAINT pk_social_accounts PRIMARY KEY (id),
	CONSTRAINT uk_social_accounts_platform_platform_account_id UNIQUE (platform, platform_account_id),
	CONSTRAINT ck_social_accounts_platform CHECK (platform IN ('FACEBOOK', 'TWITTER')),
	CONSTRAINT ck_social_accounts_followers CHECK (followers >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Bài viết; không lưu platform (lấy từ social_accounts) để tránh lệch dữ liệu
CREATE TABLE posts (
	id                BIGINT       NOT NULL AUTO_INCREMENT,
	social_account_id BIGINT       NOT NULL,
	platform_post_id  VARCHAR(100) NOT NULL,
	post_url          VARCHAR(500) NULL,
	content           TEXT         NULL,
	published_at      DATETIME(6)  NULL,
	-- NULL khi bài do job crawl tạo (không có người import)
	imported_by       BIGINT       NULL,
	created_at        DATETIME(6)  NOT NULL,
	updated_at        DATETIME(6)  NOT NULL,
	CONSTRAINT pk_posts PRIMARY KEY (id),
	CONSTRAINT uk_posts_social_account_id_platform_post_id UNIQUE (social_account_id, platform_post_id),
	CONSTRAINT fk_posts_social_account_id FOREIGN KEY (social_account_id) REFERENCES social_accounts (id),
	CONSTRAINT fk_posts_imported_by FOREIGN KEY (imported_by) REFERENCES users (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_posts_published_at ON posts (published_at);

-- Snapshot chỉ số của bài viết theo thời gian: chỉ thêm, không sửa
CREATE TABLE social_metrics (
	id           BIGINT      NOT NULL AUTO_INCREMENT,
	post_id      BIGINT      NOT NULL,
	likes        BIGINT      NOT NULL DEFAULT 0,
	shares       BIGINT      NOT NULL DEFAULT 0,
	comments     BIGINT      NOT NULL DEFAULT 0,
	collected_at DATETIME(6) NOT NULL,
	CONSTRAINT pk_social_metrics PRIMARY KEY (id),
	-- Xóa bài thì DB xóa luôn snapshot
	CONSTRAINT fk_social_metrics_post_id FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
	CONSTRAINT ck_social_metrics_counts CHECK (likes >= 0 AND shares >= 0 AND comments >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Lấy snapshot mới nhất và chuỗi theo thời gian của bài
CREATE INDEX idx_social_metrics_post_id_collected_at ON social_metrics (post_id, collected_at);
