package org.mitrofan.bookstore;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByIsbn(String isbn);
}
