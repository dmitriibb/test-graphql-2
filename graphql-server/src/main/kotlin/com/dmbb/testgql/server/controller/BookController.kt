package com.dmbb.testgql.server.controller

import com.dmbb.testgql.server.model.Author
import com.dmbb.testgql.server.model.Book
import com.dmbb.testgql.server.service.AuthorService
import com.dmbb.testgql.server.service.BookService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class BookController(
    private val bookService: BookService,
    private val authorService: AuthorService,
) {

    @QueryMapping
    fun bookById(@Argument id: String): Book? = bookService.getById(id)

    @SchemaMapping(field = "author", typeName = "Book")
    fun author(book: Book): Author? = authorService.getById(book.author)
}
