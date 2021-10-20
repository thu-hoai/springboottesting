package com.epam.springbootpracticing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.springbootpracticing.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
