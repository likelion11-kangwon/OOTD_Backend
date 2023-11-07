// PostService.java
package com.grow_project_backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.grow_project_backend.dto.AllPostsDto;
import com.grow_project_backend.dto.CreatePostDto;
import com.grow_project_backend.dto.PostDto;
import com.grow_project_backend.dto.UpdatePostDto;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
    private PostRepository postRepository;
	
	@Autowired
    private AmazonS3 s3Client;
	
	@Value("${application.bucket.name}")
    private String bucketName;

	// 생성
    public PostDto createPost(CreatePostDto createPostDto, HttpSession session, MultipartFile file) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setTitle(createPostDto.getPostTitle());
        postEntity.setContents(createPostDto.getPostContents());
        postEntity.setCategory(createPostDto.getPostCategory());
        
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            postEntity.setPostImageUrl(fileUrl);
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.");
        }
        
        PostEntity savedPost = postRepository.save(postEntity);

        return new PostDto(
            savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getContents(),
            savedPost.getCategory(),
            savedPost.getLikedUsers().contains(user),
            savedPost.getPostImageUrl()
        );
    }
    
    // 읽기
    public PostDto getPostById(Long id, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        return new PostDto(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory(),
            postEntity.getLikedUsers().contains(user),
            postEntity.getPostImageUrl()
        );
    }
    
    // 모두 읽기
    public List<AllPostsDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<AllPostsDto> postDtos = postEntities.stream().map(postEntity -> new AllPostsDto(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory(),
        	postEntity.getPostImageUrl())
        ).collect(Collectors.toList());
        return postDtos;
    }
    
    // 수정
    public PostDto updatePost(Long id, UpdatePostDto updatePostDto, HttpSession session, MultipartFile file) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postEntity.setTitle(updatePostDto.getPostTitle());
        postEntity.setContents(updatePostDto.getPostContents());
        postEntity.setCategory(updatePostDto.getPostCategory());

        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            postEntity.setPostImageUrl(fileUrl);
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.");
        }
        
        PostEntity savedPost = postRepository.save(postEntity);

        return new PostDto(
            savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getContents(),
            savedPost.getCategory(),
            savedPost.getLikedUsers().contains(user),
            savedPost.getPostImageUrl()
        );
    }
    
    // 삭제
    public void deletePost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));
        postRepository.delete(postEntity);
    }
}
