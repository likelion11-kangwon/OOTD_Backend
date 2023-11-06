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
@Table(name = "Comment")
@Entity
public class CommentEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
	
	@ManyToOne
    @JoinColumn(name = "userId")
	private UserEntity user;
	
	@ManyToOne
    @JoinColumn(name = "postId")
	private PostEntity post;
	
	private String commentContents;
}
