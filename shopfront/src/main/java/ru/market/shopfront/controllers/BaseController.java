package ru.market.shopfront.controllers;

import java.util.function.Function;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.market.shopfront.utils.SecurityUtils;

public class BaseController {

  protected Mono<Rendering> executeWithUsername(Function<String, Mono<Rendering>> method) {
    return SecurityUtils.getUsername().flatMap(username -> {
      if (username.isEmpty()) {
        return method.apply(null);
      }

      return method.apply(username);
    });
  }

}
