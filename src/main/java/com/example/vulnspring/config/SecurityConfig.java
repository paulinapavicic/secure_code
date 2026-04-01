package com.example.vulnspring.config;


import com.example.vulnspring.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableScheduling
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/auth/**",           // JWT
                        "/", "/home", "/login", "/logout",
                        "/update", "/transfer", "/checkdb",
                        "/issue", "/support", "/token", "/address",
                        "/**/*.html", "/**/*.css", "/**/*.js",  // Static resources
                        "/error"                   // Error pages
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin();
    }
    @Scheduled(fixedDelay = 300000)  // Every 5 minutes
    public void cleanupExpiredTokens() {
        refreshTokenService.cleanupExpiredTokens();
    }
}
