package org.mitrofan.bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

@DataMongoTest
class BookRepositoryTests {
    @Autowired
    BookRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll().block();
    }

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
                .expectNextMatches(b -> book.getIsbn().equals(b.getIsbn()) &&
                        book.getTitle().equals(b.getTitle()) &&
                        book.getAuthorFirstLastName().equals(b.getAuthorFirstLastName()) &&
                        book.getAddedOn().equals(b.getAddedOn()))
                .expectComplete()
                .verify();
    }

    @Test
    void testSaveDoesntAllowBooksWithSameIsbn() {
        Book book1 = Book.builder()
                .isbn("isbn")
                .addedOn(new Date())
                .title("title1")
                .authorFirstLastName("First1 Last1")
                .build();

        Book book2 = Book.builder()
                .isbn("isbn")
                .addedOn(new Date())
                .title("title2")
                .authorFirstLastName("First2 Last2")
                .build();

        var saved = repository.saveAll(Flux.just(book1.toBuilder().build(), book2.toBuilder().build()));

        StepVerifier.create(saved)
                .expectNextCount(1)
                .expectError(DuplicateKeyException.class)
                .verify();
    }
}
