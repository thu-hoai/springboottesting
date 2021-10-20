package com.epam.springbootpracticing.service;

import java.util.List;

import com.epam.springbootpracticing.dto.UserDto;
import com.epam.springbootpracticing.security.JwtUserDetails;

public interface IUserService {

	public JwtUserDetails findUserById(Long id);

	public JwtUserDetails createUser(UserDto userDto);

	public List<JwtUserDetails> findAllUsers();

	public void deleteUserById(Long id);

	public JwtUserDetails updateUser(UserDto userDto);

}
