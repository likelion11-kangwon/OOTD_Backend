package com.grow_project_backend.controller;

import com.grow_project_backend.dto.LikeUserNumberDto;
import com.grow_project_backend.dto.PostLikeDto;
import com.grow_project_backend.dto.ResponseLikedPostListDto;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.UserRepository;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/like")
public class LikeController {
  private final UserLikedPostService userLikedPostService;
  private final UserRepository userRepository;

  @Autowired
  public LikeController(UserLikedPostService userLikedPostService, UserRepository userRepository) {
    this.userLikedPostService = userLikedPostService;
    this.userRepository = userRepository;
  }

  @GetMapping("/update/{postId}")
  public ResponseEntity<PostLikeDto> addOrCancelLike(@PathVariable Long postId, HttpSession session) {
      PostLikeDto result = userLikedPostService.addOrCancelLikeUser(postId, session);
      return new ResponseEntity<>(result, HttpStatus.OK);
  }
  @GetMapping("/post/{postId}")
  public ResponseEntity<LikeUserNumberDto> getPostLikedUserNumber(@PathVariable Long postId) {
    LikeUserNumberDto result = new LikeUserNumberDto();
    result.setLikeUserNumber(userLikedPostService.getLikeUserNumber(postId));
    result.setPostId(postId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  @GetMapping("/user/{userId}")
  public ResponseEntity<ResponseLikedPostListDto> getUserLikedPosts(@PathVariable Long userId, HttpSession session) {
      UserEntity user = userRepository.findById(userId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
      session.setAttribute("user", user);

      ResponseLikedPostListDto result = userLikedPostService.getLikePosts(session);
      return new ResponseEntity<>(result, HttpStatus.OK);
  }
}