package com.example.ReservationSystem.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authentificationProvider;
    private AuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").usernameParameter("email")
                .defaultSuccessUrl("/main")
                .loginProcessingUrl("/login").usernameParameter("email")
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authenticationProvider(authentificationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


//        http.csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/**")
//                .permitAll()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
//                .and()
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/main")
//                        .loginProcessingUrl("/login")
//                        .failureUrl("/login?error")
//                        .permitAll()
//                ).logout(
//                        logout -> logout
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
//
//                );
//
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                .and()
//                .authenticationProvider(authentificationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//        http
//                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(authEntryPoint)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();

//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
