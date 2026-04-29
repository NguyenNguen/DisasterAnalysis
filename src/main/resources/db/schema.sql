CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    disaster_name VARCHAR(255) NOT NULL,
    keywords TEXT NOT NULL,
    hashtags TEXT NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    platforms VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: posts
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    platform_id VARCHAR(255) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(255),
    published_at TIMESTAMP NOT NULL,
    url VARCHAR(500),
    preprocessed_content TEXT,
    sentiment VARCHAR(20),
    damage_categories VARCHAR(255),
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (project_id, platform_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Indexes for posts table
CREATE INDEX IF NOT EXISTS idx_posts_project_id ON posts(project_id);
CREATE INDEX IF NOT EXISTS idx_posts_published_at ON posts(published_at);
CREATE INDEX IF NOT EXISTS idx_posts_platform_id ON posts(platform_id);
CREATE INDEX IF NOT EXISTS idx_posts_sentiment ON posts(sentiment);

-- Table: comments
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    platform_id VARCHAR(255) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(255),
    published_at TIMESTAMP NOT NULL,
    preprocessed_content TEXT,
    sentiment VARCHAR(20),
    damage_categories VARCHAR(255),
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (project_id, post_id, platform_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Indexes for comments table
CREATE INDEX IF NOT EXISTS idx_comments_post_id ON comments(post_id);
CREATE INDEX IF NOT EXISTS idx_comments_project_id ON comments(project_id);
CREATE INDEX IF NOT EXISTS idx_comments_published_at ON comments(published_at);
CREATE INDEX IF NOT EXISTS idx_comments_platform_id ON comments(platform_id);
CREATE INDEX IF NOT EXISTS idx_comments_sentiment ON comments(sentiment);

-- Table: ai_summaries
CREATE TABLE IF NOT EXISTS ai_summaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    summary_text TEXT NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    posts_analyzed INT NOT NULL,
    comments_analyzed INT NOT NULL,
    model VARCHAR(50) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Index for ai_summaries table
CREATE INDEX IF NOT EXISTS idx_ai_summaries_project_id ON ai_summaries(project_id);
