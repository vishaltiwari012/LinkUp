package com.cw.linkedin.post_service.event;

import lombok.Data;

@Data
public class PostLikedEvent {
    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
