package com.cw.linkedin.post_service.event;


import lombok.Data;

@Data
public class PostCreatedEvent {
    private Long creatorId;
    private String content;
    private Long postId;
}
