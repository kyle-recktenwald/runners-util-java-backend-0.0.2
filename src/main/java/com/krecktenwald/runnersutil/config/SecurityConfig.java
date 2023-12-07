package com.krecktenwald.runnersutil.config;

import com.krecktenwald.runnersutil.security.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
  public static final String ADMIN = "admin";
  public static final String USER = "user";

  private final JwtAuthConverter jwtAuthConverter;

  /*@Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/auth/user-role/is-anonymous",
                        "/api/auth/user-role/is-anonymous/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/auth/user-role/is-admin",
                        "/api/auth/user-role/is-admin/**")
                    .hasRole(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/auth/user-role/is-user")
                    .hasAnyRole(ADMIN, USER)
                    .anyRequest()
                    .authenticated())
        .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
        .rememberMe(Customizer.withDefaults());

    return http.build();
  }*/
}
