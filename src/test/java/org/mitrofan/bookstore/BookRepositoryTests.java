package org.mitrofan.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
class BookRepositoryTests {
    @Autowired
    BookRepository repository;

    @Test
    void testSaves() {
        var saved = repository.save(Book.builder().build());

        StepVerifier.create(saved)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }
}
