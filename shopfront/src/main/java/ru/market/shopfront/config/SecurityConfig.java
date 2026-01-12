package ru.market.shopfront.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;

// TODO replace in-memory storage to keycloak
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSessionServerCsrfTokenRepository csrfTokenRepository() {
    return new WebSessionServerCsrfTokenRepository();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("user")
        .password(passwordEncoder().encode("password"))
        .roles("USER")
        .build();
    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  public RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler() {
    RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
    logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/items"));
    return logoutSuccessHandler;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                          RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler) {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
        )
        .formLogin(form -> form
            .loginPage("/login")
            .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/items"))
        )
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
        )
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers("/login", "/items/**", "/items/image/**").permitAll()
            .anyExchange().authenticated()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(redirectServerLogoutSuccessHandler)
        )
        .oauth2Client(withDefaults());
    return http.build();
  }

}