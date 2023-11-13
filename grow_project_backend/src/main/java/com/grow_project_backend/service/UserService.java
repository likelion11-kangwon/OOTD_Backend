package com.grow_project_backend.service;

import javax.servlet.http.HttpSession;

import com.grow_project_backend.dto.RequestLoginDto;
import com.grow_project_backend.dto.RequestRegisterDto;
import com.grow_project_backend.dto.ResponseLoginDto;
import com.grow_project_backend.dto.ResponseRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.UserRepository;

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
}
