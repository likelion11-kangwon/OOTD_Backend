package com.grow_project_backend.controller;

import com.grow_project_backend.dto.CommentDto;
import com.grow_project_backend.dto.CreatePostDto;
import com.grow_project_backend.dto.PostDto;
import com.grow_project_backend.dto.UserLikedPostDto;
import com.grow_project_backend.entity.CommentEntity;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/post/{postId}/like")
public class LikeController {
  private final UserLikedPostService userLikedPostService;

  @Autowired
  public LikeController(UserLikedPostService userLikedPostService) {
    this.userLikedPostService = userLikedPostService;
  }

  @PostMapping
  public ResponseEntity<UserLikedPostDto> addOrCancelLike(@PathVariable Long postId, HttpSession session) {
    int likeUserNumber = userLikedPostService.addOrCancelLikeUser(postId, session);
    UserLikedPostDto result = new UserLikedPostDto();
    result.setPostId(postId);
    result.setLikeUserNumber(likeUserNumber);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
