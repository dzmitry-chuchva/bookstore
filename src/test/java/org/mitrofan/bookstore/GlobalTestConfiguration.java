package org.mitrofan.bookstore;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
class GlobalTestConfiguration {
}
