package com.disaster.analysis.domain.entity;

import com.disaster.analysis.shared.constant.Platform;
import com.disaster.analysis.shared.constant.Sentiment;
import java.time.LocalDateTime;

public abstract class BaseContent {
    protected Long id;
    protected Long projectId;
    protected String platformId;
    protected Platform platform;
    protected String content;
    protected String author;
    protected LocalDateTime publishedAt;
    protected String preprocessedContent;
    protected Sentiment sentiment;
    protected String damageCategories;
    protected boolean isAnalyzed;
    protected LocalDateTime collectedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }

    public boolean isAnalyzed() {
        return isAnalyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        isAnalyzed = analyzed;
    }

    public String getDamageCategories() {
        return damageCategories;
    }

    public void setDamageCategories(String damageCategories) {
        this.damageCategories = damageCategories;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public String getPreprocessedContent() {
        return preprocessedContent;
    }

    public void setPreprocessedContent(String preprocessedContent) {
        this.preprocessedContent = preprocessedContent;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}