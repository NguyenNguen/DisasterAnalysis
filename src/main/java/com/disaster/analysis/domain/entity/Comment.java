package com.disaster.analysis.domain.entity;

public class Comment extends BaseContent {
    private Long postId;

    // Constructor
    public Comment() {
        super();
    }

    // Getter và Setter
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}