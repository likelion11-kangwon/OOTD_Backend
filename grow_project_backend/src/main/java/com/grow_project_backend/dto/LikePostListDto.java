package com.grow_project_backend.dto;

import com.grow_project_backend.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikePostListDto {
  private Long userId;
  private List<LikePostDto> data;
}
