package org.mitrofan.bookstore;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RequiredArgsConstructor
class BookHandler {
    private final BookRepository bookRepository;

    Mono<ServerResponse> addBook(ServerRequest request) {
        return ok().body(request.bodyToMono(Book.class).flatMap(bookRepository::save), Book.class);
    }

    Mono<ServerResponse> findBookByIsbn(ServerRequest request) {
        String isbn = request.queryParam("isbn").orElseThrow(() -> new IllegalArgumentException("isbn query parameter is required"));
        return bookRepository.findByIsbn(isbn).flatMap(book -> ok().bodyValue(book)).switchIfEmpty(ServerResponse.notFound().build());
    }
}
