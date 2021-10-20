package com.epam.springboottesting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.springboottesting.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
