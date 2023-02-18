package org.mitrofan.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@WebFluxTest(excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class BookstoreApplicationWebTests {
    @Autowired
    WebTestClient client;

    @MockBean
    BookRepository bookRepository;

    @Test
    void testAddBook() {
        given(bookRepository.save(any(Book.class))).willReturn(Mono.just(Book.builder().build()));

        client.post().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Book.class);
    }

    @Test
    void testFindByIsbn() {
        given(bookRepository.findByIsbn("isbn")).willReturn(Mono.just(Book.builder().isbn("isbn").build()));

        client.get().uri("/?isbn={isbn}", "isbn")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Book.class)
                .value(book -> assertEquals("ISBN should match", "isbn", book.getIsbn()));
    }

    @Test
    void testDoesntFindByIsbn() {
        given(bookRepository.findByIsbn("isbn")).willReturn(Mono.empty());

        client.get().uri("/?isbn={isbn}", "isbn")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

    @Test
    void testFindByIsbnError() {
        given(bookRepository.findByIsbn("isbn")).willReturn(Mono.error(new IllegalStateException()));

        client.get().uri("/?isbn={isbn}", "isbn")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
