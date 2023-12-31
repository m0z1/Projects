package com.team01.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Bean
    AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/","/member/login","/member/join").permitAll()
		.antMatchers("/member/*").authenticated()
		.antMatchers("/manager/*").hasAnyRole("MANAGER","ADMIN")
		.antMatchers("/admin/*").hasRole("ADMIN")
		.and()
		.formLogin().loginPage("/member/login").defaultSuccessUrl("/")
		.and()
		.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
		.and().exceptionHandling().accessDeniedPage("/member/login");
		
		return http.build();
	}
	
	
/*@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/user/*").authenticated().anyRequest().permitAll().and().formLogin().loginPage("/login").loginProcessingUrl("/loginPro").defaultSuccessUrl("/")
		.and().logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
	}*/
}
