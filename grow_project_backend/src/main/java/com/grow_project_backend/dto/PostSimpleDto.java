package com.grow_project_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDto {
  private Long postId;
  private String postTitle;
  private String postImageUrl;
}
