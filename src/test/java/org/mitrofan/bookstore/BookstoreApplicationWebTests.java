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

@WebFluxTest
class BookstoreApplicationWebTests {
    @Autowired
    WebTestClient client;

    @MockBean
    BookRepository bookRepository;

    @Test
    void testAddBook() {
        given(bookRepository.save(any(Book.class))).willReturn(Mono.just(Book.builder().build()));

        client.post().uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class);
    }
}
