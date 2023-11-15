package com.grow_project_backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostDetailDto {
  private Long postId;
  private String username;
  private String category;
  private String title;
  private String contents;
  private String imageUrl;
  private List<CommentDto> comments;
  private int likeUserNumber;
  private boolean isLiked;
}
