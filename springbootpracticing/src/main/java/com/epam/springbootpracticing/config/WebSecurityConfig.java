package com.epam.springbootpracticing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.epam.springbootpracticing.security.JwtAuthenticationEntryPoint;
import com.epam.springbootpracticing.security.JwtAuthorizationTokenFilter;
import com.epam.springbootpracticing.security.OAuth2AuthenticationSuccessHandler;
import com.epam.springbootpracticing.security.oauth.CustomOauthUserService;

/**
 * Security configuration
 * 
 * @author Hoai_Le
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Custom JWT based security filter
	@Autowired
	private JwtAuthorizationTokenFilter authenticationTokenFilter;
	
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    
    @Autowired
    private CustomOauthUserService customOAuth2UserService;
    
    @Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

    /**
     * Use the BCrypt password hashing function for all passwords
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }


	/**
	 * Filter chain configuration
	 */
	@Override
	protected void configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf().disable()
			.cors()
			.and()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
					.antMatchers("/h2-console/**/**").permitAll()
					.antMatchers("/auth/**", "/oauth2/**").permitAll()
					.anyRequest().authenticated()
			.and()
				.oauth2Login()
					.authorizationEndpoint().baseUri("/oauth2/authorize")
				.and()
					.redirectionEndpoint().baseUri("/oauth2/callback/*")
				.and()
					.userInfoEndpoint().userService(customOAuth2UserService)
				.and()
					.successHandler(oAuth2AuthenticationSuccessHandler);

	    // Add custom Token based authentication filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().frameOptions().sameOrigin() // required to set for H2 else H2 Console will be blank.
            .cacheControl();
	}

}
