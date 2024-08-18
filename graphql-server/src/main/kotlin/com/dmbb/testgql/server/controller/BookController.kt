package com.dmbb.testgql.server.controller

import com.dmbb.testgql.server.model.Author
import com.dmbb.testgql.server.model.Book
import com.dmbb.testgql.server.service.AuthorService
import com.dmbb.testgql.server.service.BookService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class BookController(
    private val bookService: BookService,
    private val authorService: AuthorService,
) {

    @QueryMapping
    fun bookById(@Argument id: String): Book? = bookService.getById(id)

    @QueryMapping
    fun bookByIdReactive(@Argument id: String): Mono<Book?> = Mono.just(bookService.getById(id)!!).also {
        println("respond to bookByIdReactive")
    }

    @SchemaMapping(field = "author", typeName = "Book")
    fun author(book: Book): Author? = authorService.getById(book.author)

//    @SchemaMapping(field = "author", typeName = "Book")
//    fun authors(books: Collection<Book>): Flux<Author?> = Flux.fromIterable(
//        books.map { authorService.getById(it.author)!! }
//    )
}
