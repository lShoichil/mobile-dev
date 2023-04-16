package edu.mobiledev.security;

import edu.mobiledev.service.*;
import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserService userService;

    private final AccessTokenEntryPoint accessTokenEntryPoint;

    @Bean
    public AccessTokenFilter accessTokenFilter() {
        return new AccessTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic().disable()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests(
                auth -> auth
                    .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(
                        accessTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class))
            .userDetailsService(userService)
            .headers(headers -> headers.frameOptions().sameOrigin())
            .build();
    }

}
