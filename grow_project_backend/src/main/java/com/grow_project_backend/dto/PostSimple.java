package com.grow_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSimple {
  private Long postId;
  private String category;
  private String title;
  private String contents;
  private String imageUrl;
}
