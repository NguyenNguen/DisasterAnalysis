package com.disaster.analysis.domain.entity;

import java.time.LocalDateTime;

public class AiSummary {
    private Long id;
    private Long projectId;
    private String summaryText;      // Nội dung tóm tắt do AI viết
    private LocalDateTime generatedAt;
    private int postsAnalyzed;       // Số lượng bài đăng đã dùng để tóm tắt
    private int commentsAnalyzed;    // Số lượng bình luận đã dùng để tóm tắt
    private String model;            // Tên model AI đã dùng (ví dụ: gemini-pro)

    // Constructor
    public AiSummary() {
    }

    public AiSummary(Long id, Long projectId, String summaryText,
                     int postsAnalyzed, LocalDateTime generatedAt,
                     int commentsAnalyzed, String model) {
        this.id = id;
        this.projectId = projectId;
        this.summaryText = summaryText;
        this.postsAnalyzed = postsAnalyzed;
        this.generatedAt = generatedAt;
        this.commentsAnalyzed = commentsAnalyzed;
        this.model = model;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public int getCommentsAnalyzed() {
        return commentsAnalyzed;
    }

    public void setCommentsAnalyzed(int commentsAnalyzed) {
        this.commentsAnalyzed = commentsAnalyzed;
    }

    public int getPostsAnalyzed() {
        return postsAnalyzed;
    }

    public void setPostsAnalyzed(int postsAnalyzed) {
        this.postsAnalyzed = postsAnalyzed;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}