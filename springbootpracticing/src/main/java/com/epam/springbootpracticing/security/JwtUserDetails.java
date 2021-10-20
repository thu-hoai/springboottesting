package com.epam.springbootpracticing.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.epam.springbootpracticing.domain.Role;
import com.epam.springbootpracticing.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class JwtUserDetails implements UserDetails, OAuth2User {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private String email;

	private String firstName;

	private String lastName;

	@JsonIgnore
	private String password;

	private Collection<Role> authorities;

	private Map<String, Object> attributes;

	public JwtUserDetails(Long id, String userName, String email, String password, String firstName, String lastName,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.authorities = (Collection<Role>) authorities;
	}

	public static JwtUserDetails build(User user) {
		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());

		return new JwtUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
				user.getFirstName(), user.getLastName(), authorities);
	}

	@Override
	public Collection<Role> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonIgnore
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@JsonIgnore
	@Override
	public String getName() {
		return String.valueOf(id);
	}

}
