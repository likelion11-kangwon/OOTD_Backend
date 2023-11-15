package com.grow_project_backend.controller;

import com.grow_project_backend.dto.RequestLoginDto;
import com.grow_project_backend.dto.RequestRegisterDto;
import com.grow_project_backend.service.UserService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns="http://localhost:3000",  allowCredentials = "true")
public class AuthController {
	
	@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody RequestRegisterDto signUpDto) {
        ResponseEntity<?> signUpResponse = userService.signUp(signUpDto);
        if (signUpResponse.getStatusCode().is2xxSuccessful()) {
            // 성공 시, 회원가입된 정보를 포함한 ResponseEntity 반환
            return signUpResponse;
        } else {
            // 실패 시, 에러 메시지를 포함한 ResponseEntity 반환
            return signUpResponse;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody RequestLoginDto requestLoginDto, HttpSession session) {
        ResponseEntity<?> signInResponse = userService.signIn(requestLoginDto, session);
        if (signInResponse.getStatusCode().is2xxSuccessful()) {
            // 성공 시, 로그인 정보를 포함한 ResponseEntity 반환
            return signInResponse;
        } else {
            // 실패 시, 에러 메시지를 포함한 ResponseEntity 반환
            return signInResponse;
        }
    }
    
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
