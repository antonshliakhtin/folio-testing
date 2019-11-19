package org.folio.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
  "org.folio.service",
  "org.folio.repository.book"
})
public class ApplicationConfig {

}

