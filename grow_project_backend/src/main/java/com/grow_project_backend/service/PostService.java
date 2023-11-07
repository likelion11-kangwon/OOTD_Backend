// PostService.java
package com.grow_project_backend.service;

import com.grow_project_backend.dto.CommentDto;
import com.grow_project_backend.dto.PostSimple;
import com.grow_project_backend.dto.PostUploadDto;
import com.grow_project_backend.dto.PostDetailDto;
import com.grow_project_backend.entity.CommentEntity;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
    private PostRepository postRepository;
    
    // 생성
    public PostDetailDto createPost(PostUploadDto createPostDto, String imageUrl, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setTitle(createPostDto.getTitle());
        postEntity.setContents(createPostDto.getContents());
        postEntity.setCategory(createPostDto.getCategory());
        postEntity.setPostImageUrl(imageUrl);
        
        PostEntity savedPost = postRepository.save(postEntity);

        List<CommentDto> commentDtoList = new ArrayList<>();
        Iterator<CommentEntity> commentIterator = savedPost.getComments().iterator();

        while(commentIterator.hasNext()) {
            CommentEntity comment = commentIterator.next();
            commentDtoList.add(new CommentDto(comment.getUser().getName(), comment.getContents()));
        }

        return new PostDetailDto(savedPost.getId(), user.getName(), savedPost.getCategory(), savedPost.getTitle(),
                savedPost.getContents(), savedPost.getPostImageUrl(), commentDtoList, savedPost.getLikedUsers().size(), savedPost.getLikedUsers().contains(user)
        );
    }
    
    // 읽기
    public PostDetailDto getPostById(Long id, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        List<CommentDto> commentDtoList = new ArrayList<>();
        Iterator<CommentEntity> commentIterator = postEntity.getComments().iterator();

        while(commentIterator.hasNext()) {
            CommentEntity comment = commentIterator.next();
            commentDtoList.add(new CommentDto(comment.getUser().getName(), comment.getContents()));
        }

        return new PostDetailDto(postEntity.getId(), user.getName(), postEntity.getCategory(), postEntity.getTitle(),
                postEntity.getContents(), postEntity.getPostImageUrl(), commentDtoList, postEntity.getLikedUsers().size(), postEntity.getLikedUsers().contains(user)
        );
    }
    
    // 모두 읽기
    public List<PostSimple> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostSimple> postDtos = postEntities.stream().map(postEntity -> new PostSimple(
            postEntity.getId(),
            postEntity.getCategory(),
            postEntity.getTitle(),
            postEntity.getContents(),
        	postEntity.getPostImageUrl())
        ).collect(Collectors.toList());
        return postDtos;
    }
    
    // 수정
    public PostDetailDto updatePost(Long id, PostUploadDto updatePostDto, String imageUrl, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postEntity.setTitle(updatePostDto.getTitle());
        postEntity.setContents(updatePostDto.getContents());
        postEntity.setCategory(updatePostDto.getCategory());

        if (!imageUrl.equals("") && imageUrl != null) postEntity.setPostImageUrl(imageUrl);
        
        PostEntity updatedPost = postRepository.save(postEntity);

        List<CommentDto> commentDtoList = new ArrayList<>();
        Iterator<CommentEntity> commentIterator = postEntity.getComments().iterator();

        while(commentIterator.hasNext()) {
            CommentEntity comment = commentIterator.next();
            commentDtoList.add(new CommentDto(comment.getUser().getName(), comment.getContents()));
        }
        return new PostDetailDto(postEntity.getId(), user.getName(), postEntity.getCategory(), postEntity.getTitle(),
                postEntity.getContents(), postEntity.getPostImageUrl(), commentDtoList, postEntity.getLikedUsers().size(), postEntity.getLikedUsers().contains(user)
        );
    }
    
    // 삭제
    public void deletePost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));
        postRepository.delete(postEntity);
    }
}
