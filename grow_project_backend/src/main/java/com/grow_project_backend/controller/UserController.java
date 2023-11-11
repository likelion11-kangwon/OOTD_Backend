package com.grow_project_backend.controller;

import com.grow_project_backend.dto.PostSimple;
import com.grow_project_backend.dto.ResponseLikedPostListDto;
import com.grow_project_backend.dto.ResponseMyPageDto;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.repository.UserRepository;
import com.grow_project_backend.service.UserLikedPostService;
import com.grow_project_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(originPatterns="http://localhost:3000")
public class UserController {
  private final UserLikedPostService userLikedPostService;
  private final UserService userService;

  @Autowired
  public UserController(UserLikedPostService userLikedPostService, UserService userService) {
    this.userLikedPostService = userLikedPostService;
    this.userService = userService;
  }


  @GetMapping("/like")
  public ResponseEntity<ResponseLikedPostListDto> getUserLikedPosts(HttpSession session) {
    ResponseLikedPostListDto post = userLikedPostService.getLikePosts(session);
    return new ResponseEntity<>(post, HttpStatus.OK);
  }

  @GetMapping("/mypage")
  public ResponseEntity<ResponseMyPageDto> getMyInformation(HttpSession session) {
    ResponseMyPageDto result = userService.getMyPageData(session);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }


}