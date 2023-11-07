// PostController.java
package com.grow_project_backend.controller;

import com.grow_project_backend.dto.*;
import com.grow_project_backend.service.PostService;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, HttpSession session) {
    	PostDto post = postService.getPostById(id, session);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<PageDto> getPostList() {
        List<AllPostsDto> allPosts = postService.getAllPosts();
        Iterator<AllPostsDto> it = allPosts.iterator();
        int i = 0;
        List<PostSimpleDto> postSimpleDtoList = new ArrayList<>(4);
        List<PostsDto> postsDtos = new LinkedList<>();
        while(it.hasNext()) {
            AllPostsDto post = it.next();
            postSimpleDtoList.add(new PostSimpleDto(post.getPostId(), post.getPostTitle(), post.getPostImageUrl()));
            i = (i+1) % 4;
            if (i == 0 || !it.hasNext()) {
                postsDtos.add(new PostsDto(postSimpleDtoList.toArray(new PostSimpleDto[4])));
                postSimpleDtoList = new ArrayList<>(4);
            }
        }
        return new ResponseEntity<>(new PageDto(postsDtos.toArray(new PostsDto[0])), HttpStatus.OK);
    }

    // 모든 게시글을 읽음
    @GetMapping
    public ResponseEntity<List<AllPostsDto>> getAllPosts() {
        List<AllPostsDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody UpdatePostDto updatePostDto, HttpSession session) {
    	PostDto updatedPost = postService.updatePost(id, updatePostDto, session);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
