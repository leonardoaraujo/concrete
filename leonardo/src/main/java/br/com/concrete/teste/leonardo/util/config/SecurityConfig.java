/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.concrete.teste.leonardo.util.config.jwt.JwtConfigurer;
import br.com.concrete.teste.leonardo.util.config.jwt.JwtTokenProvider;

/**
 * The Class SecurityConfig.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/** The token provider. */
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	/**
	 * Password encoder.
	 *
	 * @return the b crypt password encoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Authentication manager bean.
	 *
	 * @return the authentication manager
	 * @throws Exception the exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * Configure.
	 *
	 * @param http the http
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/auth/create", "/auth/signin", "/api-docs/**", "/swagger-ui.html**", "/swagger-ui.html/**").permitAll()
				.antMatchers("/contas/**", "/contas**").authenticated()
			.and()
			.apply(new JwtConfigurer(tokenProvider));
	}

}
