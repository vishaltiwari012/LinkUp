package com.cw.linkedin.post_service.service.impl;

import com.cw.linkedin.post_service.auth.UserContextHolder;
import com.cw.linkedin.post_service.clients.ConnectionsClient;
import com.cw.linkedin.post_service.dto.PersonDto;
import com.cw.linkedin.post_service.dto.PostCreateRequestDto;
import com.cw.linkedin.post_service.dto.PostDto;
import com.cw.linkedin.post_service.entity.Post;
import com.cw.linkedin.post_service.event.PostCreatedEvent;
import com.cw.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.cw.linkedin.post_service.repository.PostRepository;
import com.cw.linkedin.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;
    @Override
    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        log.info("Creating post...");
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postRepository.save(post);

//        Sent the message
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();
        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with id : {}",postId);
        Long userId = UserContextHolder.getCurrentUserId();
        List<PersonDto> firstConnections = connectionsClient.getFirstConnections();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + postId));

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }
}
