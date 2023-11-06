package com.grow_project_backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "Post")
@Entity
public class PostEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
	
	@ManyToOne
    @JoinColumn(name = "userId")
	private UserEntity user;
	
	private String postTitle;
	
	private String postContents;
	
	private String postCategory;
	
	//private String postImageUrl;
}
