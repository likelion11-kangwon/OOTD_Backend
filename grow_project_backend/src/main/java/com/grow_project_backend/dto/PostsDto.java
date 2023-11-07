package com.grow_project_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsDto {
  private PostSimpleDto[] postSimpleDtos;
}
