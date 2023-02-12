package org.mitrofan.bookstore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
class Book {
    @Id
    @TextIndexed
    private String isbn;
    @Version
    private long version;
    @TextIndexed
    private String author;
    @CreatedDate
    private Instant addedOn;
    @TextIndexed
    private String title;
}
