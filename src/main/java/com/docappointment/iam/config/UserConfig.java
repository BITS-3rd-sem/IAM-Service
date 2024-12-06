package com.docappointment.iam.config;

import com.docappointment.iam.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class UserConfig implements WebMvcConfigurer {
	
	@Autowired
	@Lazy
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	@Lazy
	private JwtAuthenticationFilter jwtAuthFilter;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	private UserDao repo;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
                .authorizeHttpRequests(auth ->
                        auth
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.requestMatchers("/api/v1/auth/token", "/api/v1/auth/login", "/api/v1/auth/validate", "/api/v1/auth/register", "/h2-console/**", "/api/v1/specialization").permitAll()
								.requestMatchers(toH2Console()).permitAll()
								.requestMatchers(HttpMethod.POST, "/api/v1/doctor/**").hasAnyAuthority("ADMIN")
								.requestMatchers(HttpMethod.PUT, "/api/v1/doctor/**").hasAnyAuthority("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/api/v1/doctor/**").hasAnyAuthority("ADMIN")
								.anyRequest().authenticated()
                )
                .csrf(c -> c.disable())
				.headers(httpSecurityHeadersConfigurer -> {
					httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> {
						frameOptionsConfig.disable();
					});
				})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .httpBasic(withDefaults())
                .build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> repo.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}