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
public class CategoryService {
    @Autowired
    private PostRepository postRepository;

    public List<PostSimple> getSortedPostList(String tab) {
        List<PostEntity> allCategory = postRepository.findByCategoryContaining(tab);
        List<PostSimple> selectedTab = new ArrayList<>();
        Iterator<PostEntity> allCategoryIt = allCategory.iterator();
        while (allCategoryIt.hasNext()) {
            PostEntity post = allCategoryIt.next();
            selectedTab.add(new PostSimple(post.getId(), post.getCategory(), post.getTitle(), post.getContents(),
                    post.getPostImageUrl()));
        }
        return selectedTab;
    }
}
