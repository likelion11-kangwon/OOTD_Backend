package com.grow_project_backend.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grow_project_backend.dto.PostSimple;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.repository.PostRepository;

@Service
public class SearchService {
	@Autowired
	private PostRepository postRepository;
	
	
	public List<PostSimple> getSearchPostList(String keyword){
		List<PostEntity> allPost =  postRepository.findByTitleContaining(keyword);
		List<PostSimple> result = new ArrayList<>();
		Iterator<PostEntity> allPostIt = allPost.iterator();
		while(allPostIt.hasNext()) {
			PostEntity post = allPostIt.next();
			result.add(new PostSimple(post.getId(), post.getCategory(), post.getTitle(), post.getContents(), post.getPostImageUrl()));
		}

		return result;
	}
}
