package com.epam.springboottesting.service;

import java.util.List;

import com.epam.springboottesting.dto.UserDto;
import com.epam.springboottesting.security.JwtUserDetails;

public interface IUserService {

	public JwtUserDetails findUserById(Long id);

	public JwtUserDetails createUser(UserDto userDto);

	public List<JwtUserDetails> findAllUsers();

	public void deleteUserById(Long id);

	public JwtUserDetails updateUser(UserDto userDto);

}
