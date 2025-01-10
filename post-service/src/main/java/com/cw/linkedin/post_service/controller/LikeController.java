package com.cw.linkedin.post_service.controller;

import com.cw.linkedin.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        postLikeService.likePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
        postLikeService.unlikePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
