package com.cw.linkedin.post_service.service.impl;

import com.cw.linkedin.post_service.auth.UserContextHolder;
import com.cw.linkedin.post_service.entity.Post;
import com.cw.linkedin.post_service.entity.PostLike;
import com.cw.linkedin.post_service.event.PostLikedEvent;
import com.cw.linkedin.post_service.exceptions.BadRequestException;
import com.cw.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.cw.linkedin.post_service.repository.PostLikeRepository;
import com.cw.linkedin.post_service.repository.PostRepository;
import com.cw.linkedin.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;
    @Override
    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like the post with id : {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + postId));


        boolean isAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(isAlreadyLiked) {
            throw new BadRequestException("Cannot like the same post again");
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id : {} liked successfully",postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();
        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
    }

    @Override
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike the post with id : {}", postId);
        boolean isExist = postRepository.existsById(userId);
        if(!isExist) {
            throw new ResourceNotFoundException("Post not found with id : " + postId);
        }

        boolean isAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!isAlreadyLiked) {
            throw new BadRequestException("Cannot unlike the post which is not liked");
        }

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post with id : {} unliked successfully",postId);
    }
}
