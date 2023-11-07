package com.grow_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPagesDto {
  private List<PostSimple[]> postsSimple;
}
