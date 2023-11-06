package com.grow_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
	private String userLoginId;
	private String userPassword;
	private String userPasswordCheck;
	private String userName;
}
