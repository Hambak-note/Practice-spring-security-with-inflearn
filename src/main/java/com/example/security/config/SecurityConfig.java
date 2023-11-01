package com.example.security.config;

import com.example.security.config.jwt.JwtAuthenticationFilter;
import com.example.security.config.jwt.JwtAuthorizationFilter;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig  {

    private final CorsConfig corsConfig;

    private final UserRepository userRepository;

    //@Bean 선언해주면 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.


    //security 연습 설정용
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/user/**").authenticated()
//                                .requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                                .anyRequest().permitAll());
//        http
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/loginForm")
//                                .usernameParameter("username")
//                                .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
//                                .defaultSuccessUrl("/"));
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 사용하지 않겠다는 의미
                .formLogin(FormLoginConfigurer::disable) //폼 로그인 사용안함
                .httpBasic(HttpBasicConfigurer::disable) //기본적인 http 로그인 방식을 사용하지 않음
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/manager/**").hasAnyRole( "ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/admin/**").hasAnyRole( "ADMIN")
                                .anyRequest().permitAll())
                .apply(new MyCustomDsl());

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter()) //@CrossOrigin(인증x), 시큐리티 필터에 등록 인증(O)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }




}
