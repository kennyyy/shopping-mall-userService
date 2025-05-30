package com.mallproject.UserService.config;

import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.security.CustomLoginFilter;
import com.mallproject.UserService.security.JWTService;
import com.mallproject.UserService.security.JwtAuthrizationFilter;
import com.mallproject.UserService.security.MyUserDetailsService;
import com.mallproject.UserService.security.handler.OAuthLoginFailureHandler;
import com.mallproject.UserService.security.handler.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final JWTService jwtService;

    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().configurationSource(corsConfigurationSource());

        http.addFilter(jwtAuthrizationFilter())
                .addFilter(customLoginFilter());

        http.authorizeHttpRequests()
                .antMatchers("/api/user/guest/**", "/api/user/public/**",
                        "/api/user/env", "/api/user/login").permitAll()
                .antMatchers("/api/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/user/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.oauth2Login(oauth ->{
            oauth
                    .successHandler(oAuthLoginSuccessHandler())
                    .failureHandler(oAuthLoginFailureHandler());
        });

        return http.build();
    }

    @Bean
    JwtAuthrizationFilter jwtAuthrizationFilter() throws Exception {
        return new JwtAuthrizationFilter(authenticationManager(), jwtService);
    }

    @Bean
    CustomLoginFilter customLoginFilter() throws Exception {
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(authenticationManager(), tokenMapper, jwtService);
        //기본값은 /login인데 원하는 로그인 URI지정
        customLoginFilter.setFilterProcessesUrl("/api/user/login");
        return customLoginFilter;
    }

    @Bean
    OAuthLoginSuccessHandler oAuthLoginSuccessHandler(){
        return new OAuthLoginSuccessHandler(userMapper, tokenMapper, jwtService);
    }

    @Bean
    OAuthLoginFailureHandler oAuthLoginFailureHandler(){
        return new OAuthLoginFailureHandler();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); //모든요청 메서드를 허용
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));//기본적으로 모든 헤더값은 노출할 수없다, 고로 허용해줘야한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
