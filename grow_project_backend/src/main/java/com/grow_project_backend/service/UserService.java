package com.grow_project_backend.service;

import javax.servlet.http.HttpSession;

import com.grow_project_backend.dto.*;
import com.grow_project_backend.entity.PostEntity;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<?> signUp(RequestRegisterDto signUpDto) {
        if (userRepository.existsByLoginId(signUpDto.getLoginId())) {
            return ResponseEntity.badRequest().body("이미 사용중인 아이디 입니다.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(signUpDto.getLoginId());
        userEntity.setPassword(signUpDto.getPassword()); // In real-world, this password should be encoded.
        userEntity.setName(signUpDto.getUsername());
        userRepository.save(userEntity);

        return ResponseEntity.ok(new ResponseRegisterDto(true));
    }

    public ResponseEntity<?> signIn(RequestLoginDto requestLoginDto, HttpSession session) {
        UserEntity userEntity = userRepository.findByLoginId(requestLoginDto.getLoginId());
        if (userEntity == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        if (!userEntity.getPassword().equals(requestLoginDto.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
        session.setAttribute("user", userEntity);
        
        ResponseLoginDto loginResponseDto = new ResponseLoginDto(userEntity.getId(), userEntity.getLoginId(), userEntity.getName());
        return ResponseEntity.ok(loginResponseDto);
    }

    public ResponseMyPageDto getMyPageData(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 마이페이지를 볼 수 있습니다.");
        user = userRepository.findByLoginId(user.getLoginId());

        List<PostEntity> postList = user.getPosts();
        Iterator<PostEntity> postIt = postList.iterator();
        List<PostSimple> postSimples = new ArrayList<>();
        while(postIt.hasNext()) {
            PostEntity post = postIt.next();
            postSimples.add(new PostSimple(post.getId(), post.getCategory(), post.getTitle(), post.getContents(), post.getPostImageUrl()));
        }

        List<PostEntity> likeList = user.getLikedPosts();
        Iterator<PostEntity> likeIt = likeList.iterator();
        List<PostSimple> likeSimples = new ArrayList<>();

        while(likeIt.hasNext()) {
            PostEntity like = likeIt.next();
            likeSimples.add(new PostSimple(like.getId(), like.getCategory(), like.getTitle(), like.getContents(), like.getPostImageUrl()));
        }

        ResponseMyPageDto result = ResponseMyPageDto.builder()
                .username(user.getName())
                .myPostList(postSimples)
                .myLikeList(likeSimples).build();
        return result;
    }

}
