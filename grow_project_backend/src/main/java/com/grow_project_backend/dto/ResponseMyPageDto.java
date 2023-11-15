package com.grow_project_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseMyPageDto {
  private String username;
  private List<PostSimple> myPostList;
  private List<PostSimple> myLikeList;
}
