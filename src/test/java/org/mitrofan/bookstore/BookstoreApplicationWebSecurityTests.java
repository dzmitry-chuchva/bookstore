package org.mitrofan.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@WebFluxTest
class BookstoreApplicationWebSecurityTests {
    @Autowired
    WebTestClient client;

    @MockBean
    BookRepository bookRepository;

    @Test
    void testAddBook_forbiddenByDefault() {
        given(bookRepository.save(any(Book.class))).willReturn(Mono.just(Book.builder().build()));

        client
                .post().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void testAddBookWithCsrf_unauthorized() {
        given(bookRepository.save(any(Book.class))).willReturn(Mono.just(Book.builder().build()));

        client
                .mutateWith(csrf())
                .post().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testAddBookWithCsrfAndJWT_ok() {
        given(bookRepository.save(any(Book.class))).willReturn(Mono.just(Book.builder().build()));

        client
                .mutateWith(csrf())
                .mutateWith(mockJwt())
                .post().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testFindByIsbnWithCsrf_unauthorized() {
        given(bookRepository.findByIsbn("isbn")).willReturn(Mono.just(Book.builder().isbn("isbn").build()));

        client
                .mutateWith(csrf())
                .get().uri("/?isbn={isbn}", "isbn")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testFindByIsbnWithCsrfAndJWT_ok() {
        given(bookRepository.findByIsbn("isbn")).willReturn(Mono.just(Book.builder().isbn("isbn").build()));

        client
                .mutateWith(csrf())
                .mutateWith(mockJwt())
                .get().uri("/?isbn={isbn}", "isbn")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}
