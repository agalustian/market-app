package ru.market.shopfront.utils;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class SecurityUtils {

  private SecurityUtils() {
  }

  public static Mono<String> getUsername() {
    return ReactiveSecurityContextHolder.getContext().map(securityContext -> {
      if (securityContext == null) {
        return "";
      }

      Authentication authentication = securityContext.getAuthentication();

      if (authentication == null) {
        return "";
      }

      if (authentication.getPrincipal() instanceof UserDetails userDetails) {
        return userDetails.getUsername();
      }

      return authentication.getName();
    }).defaultIfEmpty("");
  }

}
