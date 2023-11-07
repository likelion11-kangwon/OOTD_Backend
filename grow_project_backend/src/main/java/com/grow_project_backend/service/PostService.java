// PostService.java
package com.grow_project_backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.grow_project_backend.dto.CommentDto;
import com.grow_project_backend.dto.PostSimple;
import com.grow_project_backend.dto.PostUploadDto;
import com.grow_project_backend.dto.PostDetailDto;
import com.grow_project_backend.entity.CommentEntity;
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
import java.util.ArrayList;
import java.util.Iterator;
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
    public PostDetailDto createPost(PostUploadDto createPostDto, HttpSession session, MultipartFile file) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setTitle(createPostDto.getTitle());
        postEntity.setContents(createPostDto.getContents());
        postEntity.setCategory(createPostDto.getCategory());
        
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
    public PostDetailDto updatePost(Long id, PostUploadDto updatePostDto, HttpSession session, MultipartFile file) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postEntity.setTitle(updatePostDto.getTitle());
        postEntity.setContents(updatePostDto.getContents());
        postEntity.setCategory(updatePostDto.getCategory());

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
