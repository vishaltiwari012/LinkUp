package com.cw.linkedin.post_service.service;

import com.cw.linkedin.post_service.dto.PostCreateRequestDto;
import com.cw.linkedin.post_service.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostCreateRequestDto postCreateRequestDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser(Long userId);
}
