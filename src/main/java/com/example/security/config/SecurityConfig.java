package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig  {

    //@Bean 선언해주면 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

//   @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//       http.csrf().disable();
//       http.authorizeRequests()
//               .antMatchers("/user/**").authenticated()
//               .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//               .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//               .anyRequest().permitAll();
//                .and()
//                .formLogin()
//                .loginPage("/login")
//
//   }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                                .anyRequest().permitAll());
        http
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/loginForm")
                                .usernameParameter("username")
                                .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                                .defaultSuccessUrl("/"));

        return http.build();
    }


}
