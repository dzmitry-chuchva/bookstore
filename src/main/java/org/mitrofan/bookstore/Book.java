package org.mitrofan.bookstore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
class Book {
    @Id
    private String isbn;
    @Version
    private long version;
    private String authorFirstLastName;
    @CreatedDate
    private Date addedOn;
    private String title;
}
