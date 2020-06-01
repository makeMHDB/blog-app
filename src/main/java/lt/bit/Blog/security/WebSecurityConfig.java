/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author makeMH
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;
        
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//                        .csrf().disable()
			.authorizeRequests()
				.antMatchers("/userCP").authenticated()
                                .antMatchers("/blog/*/like").authenticated()
                                .antMatchers("/blog/*/comment").authenticated()
                                .antMatchers("/blog/*/editBlog").authenticated()
                                .antMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
                                .logoutUrl("/logout")
				.permitAll()
                                .invalidateHttpSession(true)
                                .logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID")
                                .and()
                        .exceptionHandling().accessDeniedPage("/accessDenied");
                
	}
        
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
