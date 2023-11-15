// PostController.java
package com.grow_project_backend.controller;

import com.grow_project_backend.dto.*;
import com.grow_project_backend.service.CategoryService;
import com.grow_project_backend.service.PostService;
import com.grow_project_backend.service.SearchService;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserLikedPostService userLikedPostService;
    @Autowired
    private SearchService searchService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDetailDto> createPost(
            @RequestPart(name = "post", required = true) PostUploadDto createRequest,
            @RequestPart(name = "imageFile", required = false) MultipartFile file,
            HttpSession session) {
        PostDetailDto newPost = postService.createPost(createRequest, session, file);
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
        while (it.hasNext()) {
            PostSimple post = it.next();
            postSimpleDtoList.add(post);
            i = (i + 1) % 4;
            if (i == 0 || !it.hasNext()) {
                postsDtos.add(postSimpleDtoList.toArray(new PostSimple[4]));
                postSimpleDtoList = new ArrayList<>(4);
            }
        }
        return new ResponseEntity<>(new PostPagesDto(postsDtos), HttpStatus.OK);
    }

    // 특정 번호의 게시글을 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(
            @PathVariable(required = true) Long id,
            @RequestPart(name = "post", required = true) PostUploadDto updateRequest,
            @RequestPart(name = "imageFile", required = false) MultipartFile file,
            HttpSession session) {
        PostDetailDto updatedPost = postService.updatePost(id, updateRequest, session, file);
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

    @PostMapping("/search")
    public ResponseEntity<List<PostSimple>> searchPost(@RequestBody RequestSearchDto searchDto) {
        List<PostSimple> result = searchService.getSearchPostList(searchDto.getKeyword());
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/category")
    public ResponseEntity<List<PostSimple>> sortedPost(@RequestBody RequestCategoryDto categoryDto) {
        List<PostSimple> selectedTab = categoryService.getSortedPostList(categoryDto.getTab());
        return new ResponseEntity<>(selectedTab, HttpStatus.OK);
    }
}
