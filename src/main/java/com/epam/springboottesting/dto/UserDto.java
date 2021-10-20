package com.epam.springboottesting.dto;

import java.util.Collection;

import com.epam.springboottesting.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private Long id;

	private String username;

	private String email;

	private String firstName;

	private String lastName;

	private String password;

	private Collection<Role> authorities;
}
