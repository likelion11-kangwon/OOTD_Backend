package com.grow_project_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grow_project_backend.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	UserEntity findByUserLoginId(String userLoginId);

	public boolean existsByUserLoginId(String userLoginId);
}
