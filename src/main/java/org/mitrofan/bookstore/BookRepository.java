package org.mitrofan.bookstore;

import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByIsbn(String isbn);

    Flux<Book> findByAuthorAndTitle(String author, String title);

    Flux<Book> findByAddedOnBetween(Range<Instant> dateRange);
}
