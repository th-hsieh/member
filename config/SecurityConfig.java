package team3.meowie.member.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import team3.meowie.member.handler.LoginSuccessHandler;
import team3.meowie.member.handler.OAuth2LoginSuccessHandler;
import team3.meowie.member.oauth.MeowieOAtuh2UserService;

@Configuration
@ComponentScan("team3.meowie.member.security")
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private MeowieOAtuh2UserService meowieOAtuh2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuilder.authenticationProvider(authProvider());
		return authBuilder.build();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/user/center")
				.hasAuthority("ROLE_USER")
				.antMatchers("/backIndex", "/backMart", "/backForum", "/backMovies", "/backTickets", "/backStickInsect", "/backLadyBug", "/locust", "/jelly")
				.hasAuthority("ROLE_ADMIN")
				.and()
			.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error")
				.successHandler(loginSuccessHandler)
				.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(meowieOAtuh2UserService)
				.and()
				.successHandler(oAuth2LoginSuccessHandler)
				.and()
			.logout()
				.logoutSuccessUrl("/");

		
		return http.build();
	}
	
	private DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String hierarchy = "ROLE_ADMIN > ROLE_USER \n ROLE_USER > ROLE_UNVERIFIED";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}
}
