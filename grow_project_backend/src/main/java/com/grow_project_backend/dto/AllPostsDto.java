package com.grow_project_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AllPostsDto {
  private Long postId;
  private String postTitle;
  private String postContents;
  private String postCategory;
  private String postImageUrl;
}
