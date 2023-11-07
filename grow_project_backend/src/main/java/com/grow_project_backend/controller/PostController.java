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
    public ResponseEntity<PostDetailDto> createPost(@RequestBody PostUploadDto createRequest, HttpSession session) {
    	PostDetailDto newPost = postService.createPost(createRequest, "http://imageUrl", session);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }
    
    // 특정 번호의 게시글을 읽음
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable Long id, HttpSession session) {
    	PostDetailDto post = postService.getPostById(id, session);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/pages")
    public ResponseEntity<PostPagesDto> getPostList() {
        List<PostSimple> allPosts = postService.getAllPosts();
        Iterator<PostSimple> it = allPosts.iterator();
        int i = 0;
        List<PostSimple> postSimpleDtoList = new ArrayList<>(4);
        List<PostSimple[]> postsDtos = new LinkedList<>();
        while(it.hasNext()) {
            PostSimple post = it.next();
            postSimpleDtoList.add(post);
            i = (i+1) % 4;
            if (i == 0 || !it.hasNext()) {
                postsDtos.add(postSimpleDtoList.toArray(new PostSimple[4]));
                postSimpleDtoList = new ArrayList<>(4);
            }
        }
        return new ResponseEntity<>(new PostPagesDto(postsDtos), HttpStatus.OK);
    }


    // 특정 번호의 게시글을 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(@PathVariable Long id, @RequestBody PostUploadDto updatePostDto, HttpSession session) {
    	PostDetailDto updatedPost = postService.updatePost(id, updatePostDto, "http://imageUrl", session);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<PostLikeDto> getLikedInformation(@PathVariable Long id, HttpSession session) {
        return new ResponseEntity<>(userLikedPostService.getLikedInformation(id, session), HttpStatus.OK);
    }
    @PutMapping("/{id}/like")
    public ResponseEntity<PostLikeDto> updateLike(@PathVariable Long id, HttpSession session) {
        PostLikeDto postLikeDto = userLikedPostService.addOrCancelLikeUser(id, session);
        return new ResponseEntity<>(postLikeDto, HttpStatus.OK);
    }

}
