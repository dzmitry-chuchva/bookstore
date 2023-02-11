package org.mitrofan.bookstore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
class BookHandler {
    private final BookRepository bookRepository;

    Mono<ServerResponse> addBook(ServerRequest request) {
        return ok().body(request.bodyToMono(Book.class).flatMap(bookRepository::save), Book.class);
    }
}
