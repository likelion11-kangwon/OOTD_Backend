// PostController.java
package com.grow_project_backend.controller;

import com.grow_project_backend.dto.CreatePostDto;
import com.grow_project_backend.dto.PostDto;
import com.grow_project_backend.dto.UpdatePostDto;
import com.grow_project_backend.dto.LikeUserNumberDto;
import com.grow_project_backend.service.PostService;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/post")
public class PostController {
	
	@Autowired
    private PostService postService;
	@Autowired
	private UserLikedPostService userLikedPostService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto createRequest, HttpSession session) {
    	PostDto newPost = postService.createPost(createRequest, session);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }
    
    // 특정 번호의 게시글을 읽음
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
    	PostDto post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 모든 게시글을 읽음
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody UpdatePostDto updatePostDto) {
    	PostDto updatedPost = postService.updatePost(id, updatePostDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
