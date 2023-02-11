package org.mitrofan.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

@DataMongoTest
class BookRepositoryTests {
    @Autowired
    BookRepository repository;

    @Test
    void testAddBook() {
        Book book = Book.builder()
                .isbn("isbn")
                .addedOn(new Date())
                .title("title")
                .authorFirstLastName("First Last")
                .build();

        Mono<Book> bookMono = repository.save(book.toBuilder().build());

        StepVerifier.create(bookMono)
                .expectNext(book)
                .expectComplete()
                .verify();
    }
}
