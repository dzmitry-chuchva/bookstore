package org.mitrofan.bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.core.query.TextCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.data.domain.Range.Bound.inclusive;

@DataMongoTest
@Import({GlobalTestConfiguration.class, StorageConfiguration.class})
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
                .title("title")
                .author("First Last")
                .build();

        Mono<Book> bookMono = repository.save(book.toBuilder().build());

        StepVerifier.create(bookMono)
                .expectNextMatches(b -> book.getIsbn().equals(b.getIsbn()) &&
                        book.getTitle().equals(b.getTitle()) &&
                        book.getAuthor().equals(b.getAuthor()))
                .expectComplete()
                .verify();
    }

    @Test
    void testAuditing() {
        Mono<Book> bookMono = repository.save(Book.builder().build());

        StepVerifier.create(bookMono)
                .expectNextMatches(b -> b.getAddedOn() != null)
                .expectComplete()
                .verify();
    }

    @Test
    void testSaveDoesntAllowBooksWithSameIsbn() {
        Book book1 = Book.builder()
                .isbn("isbn")
                .addedOn(Instant.now())
                .title("title1")
                .author("First1 Last1")
                .build();

        Book book2 = Book.builder()
                .isbn("isbn")
                .addedOn(Instant.now())
                .title("title2")
                .author("First2 Last2")
                .build();

        var saved = repository.saveAll(Flux.just(book1.toBuilder().build(), book2.toBuilder().build()));

        StepVerifier.create(saved)
                .expectNextCount(1)
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void testFindByISBN() {
        Book book = Book.builder()
                .isbn("isbn")
                .build();

        repository.saveAll(Flux.just(book.toBuilder().build(), book.toBuilder().isbn("anotherIsbn").build())).blockLast();

        Mono<Book> found = repository.findByIsbn("isbn");

        StepVerifier.create(found)
                .expectNextMatches(b -> "isbn".equals(b.getIsbn()))
                .expectComplete()
                .verify();
    }

    @Test
    void testFindByAuthorAndTitle() {
        Book book = Book.builder()
                .author("author")
                .title("title")
                .build();

        repository.saveAll(Flux.just(book.toBuilder().build(), book.toBuilder().title("anotherTitle").build())).blockLast();

        Flux<Book> found = repository.findByAuthorAndTitle("author", "title");

        StepVerifier.create(found)
                .expectNextMatches(b -> "author".equals(b.getAuthor()) && "title".equals(b.getTitle()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByAddedOnBetween() {
        repository.saveAll(Flux.just(Book.builder().isbn("1").addedOn(Instant.now()).build(),
                        Book.builder().isbn("2").addedOn(Instant.now()).build()))
                .blockLast();

        Book first = repository.findByIsbn("1").block();
        first.setAddedOn(Instant.now().minus(Duration.ofDays(2)));
        repository.save(first).block();

        Flux<Book> found = repository.findByAddedOnBetween(Range.from(inclusive(Instant.now().minus(Duration.ofDays(1))))
                .to(inclusive(Instant.now().plus(Duration.ofDays(1)))));

        StepVerifier.create(found)
                .expectNextMatches(b -> "2".equals(b.getIsbn()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByTextCriteria() {
        repository.saveAll(Flux.just(
                        Book.builder()
                                .isbn("1111 1111")
                                .title("aaaa bbbb")
                                .author("ccc ddddd")
                                .build(),
                        Book.builder()
                                .isbn("2222 2222")
                                .title("zzzz yyyy")
                                .author("xxx hello")
                                .build()))
                .blockLast();

        Flux<Book> found = repository.findAllBy(TextCriteria.forDefaultLanguage().matching("hello"));

        StepVerifier.create(found)
                .expectNextMatches(b -> "2222 2222".equals(b.getIsbn()))
                .expectComplete()
                .verify();
    }
}
