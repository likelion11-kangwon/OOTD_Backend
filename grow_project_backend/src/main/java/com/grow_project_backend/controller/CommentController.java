package com.grow_project_backend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.grow_project_backend.dto.RequestCreateCommentDto;
import com.grow_project_backend.dto.ResponseCreateCommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grow_project_backend.entity.CommentEntity;
import com.grow_project_backend.service.CommentService;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(originPatterns="http://localhost:3000", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseCreateCommentDto> addComment(@RequestBody RequestCreateCommentDto commentDto, HttpSession session) {
    	ResponseCreateCommentDto newComment = commentService.addComment(commentDto.getPostId(), commentDto.getContents(), session);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}

