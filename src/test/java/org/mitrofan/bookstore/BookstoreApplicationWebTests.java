package org.mitrofan.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
class BookstoreApplicationWebTests {
    @Autowired
    WebTestClient client;

    @Test
    void testAddBook() {
        client.post().uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Book.builder().build()), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class);
    }
}
