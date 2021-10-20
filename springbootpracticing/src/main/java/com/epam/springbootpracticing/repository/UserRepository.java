package com.epam.springbootpracticing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.epam.springbootpracticing.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("FROM User u WHERE u.username=:username ")
	User findByUserName(String username);

}
