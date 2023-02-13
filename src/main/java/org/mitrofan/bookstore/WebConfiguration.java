package org.mitrofan.bookstore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class WebConfiguration implements WebFluxConfigurer {
    @Bean
    public BookHandler bookHandler(BookRepository bookRepository) {
        return new BookHandler(bookRepository);
    }

    @RouterOperations({
            @RouterOperation(path = "/", method = RequestMethod.POST,
                    operation = @Operation(operationId = "addBook", summary = "Add or update book", tags = {"Book"},
                            requestBody = @RequestBody(description = "Book details", required = true, content = @Content(schema = @Schema(implementation = Book.class))),
                            responses = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Book.class))),
                                    @ApiResponse(responseCode = "400", description = "Missing or invalid payload")})),
            @RouterOperation(path = "/", method = RequestMethod.GET,
                    operation = @Operation(operationId = "findByIsbn", summary = "Find a book by ISBN", tags = {"Book"},
                            parameters = {@Parameter(in = ParameterIn.QUERY, name = "isbn", description = "Book ISBN", required = true)},
                            responses = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Book.class))),
                                    @ApiResponse(responseCode = "400", description = "Missing or invalid ISBN"),
                                    @ApiResponse(responseCode = "404", description = "Book not found")}))
    })
    @Bean
    public RouterFunction<?> bookRouter(BookHandler handler) {
        return route()
                .POST("/", handler::addBook)
                .GET("/", RequestPredicates.queryParam("isbn", t -> true), handler::findBookByIsbn)
                .build();
    }
}
