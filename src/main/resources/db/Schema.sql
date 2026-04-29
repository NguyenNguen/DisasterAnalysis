-- 1. Bảng Dự án (Projects)
CREATE TABLE Projects (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    disaster_name NVARCHAR(255) NOT NULL,
    keywords NVARCHAR(MAX) NOT NULL,
    hashtags NVARCHAR(MAX) NULL,
    start_date DATETIME2,
    end_date DATETIME2,
    platforms NVARCHAR(255) NOT NULL, -- Lưu dạng "YOUTUBE,FACEBOOK"
    status NVARCHAR(50) DEFAULT 'NEW', -- NEW, CRAWLING, ANALYZING, COMPLETED
    created_at DATETIME2 DEFAULT GETDATE(),
    last_modified DATETIME2 DEFAULT GETDATE()
);

-- 2. Bảng Bài đăng (Posts)
CREATE TABLE Posts (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    project_id BIGINT NOT NULL,
    platform_id NVARCHAR(255) NOT NULL, -- ID gốc từ YouTube/FB
    platform NVARCHAR(50) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    author NVARCHAR(255),
    published_at DATETIME2 NOT NULL,
    url NVARCHAR(500),
    preprocessed_content NVARCHAR(MAX) NULL,
    sentiment NVARCHAR(20) NULL,
    damage_categories NVARCHAR(255) NULL,
    is_analyzed BIT DEFAULT 0, -- 0: Chưa phân tích, 1: Đã phân tích
    collected_at DATETIME2 DEFAULT GETDATE(),
    
    CONSTRAINT FK_Posts_Project FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE,
    CONSTRAINT UQ_Project_PlatformID UNIQUE (project_id, platform_id)
);

-- 3. Bảng Bình luận (Comments)
CREATE TABLE Comments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    post_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    platform_id NVARCHAR(255) NOT NULL,
    platform NVARCHAR(50) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    author NVARCHAR(255),
    published_at DATETIME2 NOT NULL,
    preprocessed_content NVARCHAR(MAX) NULL,
    sentiment NVARCHAR(20) NULL,
    damage_categories NVARCHAR(255) NULL,
    is_analyzed BIT DEFAULT 0,
    collected_at DATETIME2 DEFAULT GETDATE(),

    CONSTRAINT FK_Comments_Post FOREIGN KEY (post_id) REFERENCES Posts(id),
    CONSTRAINT FK_Comments_Project FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);

-- 4. Bảng Tóm tắt AI (AISummaries)
CREATE TABLE AISummaries (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    summary_text NVARCHAR(MAX) NOT NULL,
    generated_at DATETIME2 DEFAULT GETDATE(),
    posts_analyzed INT NOT NULL,
    comments_analyzed INT NOT NULL,
    model NVARCHAR(50) NOT NULL,
    
    CONSTRAINT FK_Summaries_Project FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);

-- Tạo Index để tăng tốc độ tìm kiếm và vẽ biểu đồ
CREATE INDEX idx_posts_project ON Posts(project_id);
CREATE INDEX idx_posts_sentiment ON Posts(sentiment);
CREATE INDEX idx_posts_date ON Posts(published_at);
CREATE INDEX idx_comments_post ON Comments(post_id);