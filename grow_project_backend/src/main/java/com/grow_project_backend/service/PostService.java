// PostService.java
package com.grow_project_backend.service;

import com.grow_project_backend.dto.CreatePostDto;
import com.grow_project_backend.dto.PostDto;
import com.grow_project_backend.dto.UpdatePostDto;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
    private PostRepository postRepository;
    
    // 생성
    public PostDto createPost(CreatePostDto createPostDto, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setTitle(createPostDto.getPostTitle());
        postEntity.setContents(createPostDto.getPostContents());
        postEntity.setCategory(createPostDto.getPostCategory());
        
        PostEntity savedPost = postRepository.save(postEntity);

        return new PostDto(
            savedPost.getTitle(),
            savedPost.getContents(),
            savedPost.getCategory()
        );
    }
    
    // 읽기
    public PostDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        return new PostDto(
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory()
        );
    }
    
    // 모두 읽기
    public List<PostDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostDto> postDtos = postEntities.stream().map(postEntity -> new PostDto(
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory())
        ).collect(Collectors.toList());
        return postDtos;
    }
    
    // 수정
    public PostDto updatePost(Long id, UpdatePostDto updatePostDto) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postEntity.setTitle(updatePostDto.getPostTitle());
        postEntity.setContents(updatePostDto.getPostContents());
        postEntity.setCategory(updatePostDto.getPostCategory());
        
        PostEntity updatedPost = postRepository.save(postEntity);

        return new PostDto(
            updatedPost.getTitle(),
            updatedPost.getContents(),
            updatedPost.getCategory()
        );
    }
    
    // 삭제
    public void deletePost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postRepository.delete(postEntity);
    }
}
