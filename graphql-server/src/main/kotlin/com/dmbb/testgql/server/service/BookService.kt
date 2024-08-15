package com.dmbb.testgql.server.service

import com.dmbb.testgql.server.model.Book
import org.springframework.stereotype.Service

@Service
class BookService {

    val books = listOf(
        Book("book-1", "Effective Java", 416, "author-1"),
        Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
        Book("book-3", "Down Under", 436, "author-3")
    )

    fun getById(id: String): Book? = books.find { it.id == id }

}

