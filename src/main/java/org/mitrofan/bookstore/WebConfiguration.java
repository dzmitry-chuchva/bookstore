package org.mitrofan.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class WebConfiguration implements WebFluxConfigurer {
    @Bean
    public BookHandler bookHandler(BookRepository bookRepository) {
        return new BookHandler(bookRepository);
    }

    @Bean
    public RouterFunction<?> bookRouter(BookHandler handler) {
        return route()
                .POST("/", handler::addBook)
                .build();
    }
}
