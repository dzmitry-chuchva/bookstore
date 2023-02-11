package org.mitrofan.bookstore;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
