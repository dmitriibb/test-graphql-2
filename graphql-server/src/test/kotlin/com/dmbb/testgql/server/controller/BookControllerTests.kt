package com.dmbb.testgql.server.controller

import com.dmbb.testgql.server.service.AuthorService
import com.dmbb.testgql.server.service.BookService
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.graphql.test.tester.GraphQlTester
import kotlin.test.Test

@GraphQlTest(BookController::class)
class BookControllerTests {

    private val realBookService = BookService()
    private val realAuthorService = AuthorService()

    @MockBean
    val bookService: BookService? = null

    @MockBean
    val authorService: AuthorService? = null

    @Autowired
    val graphQlTester: GraphQlTester? = null


    @Test
    fun shouldGetFirstBook() {
        `when`(bookService!!.getById("book-1")).thenReturn(realBookService.getById("book-1"))
        `when`(authorService!!.getById("author-1")).thenReturn(realAuthorService.getById("author-1"))
        graphQlTester!!
            .documentName("bookDetails")
            .variable("id", "book-1")
            .execute()
            .path("bookById")
            .matchesJson(
                """
                    {
                        "id": "book-1",
                        "name": "Effective Java",
                        "pageCount": 416,
                        "author": {
                          "firstName": "Joshua",
                          "lastName": "Bloch"
                        }
                    }
                
                """.trimIndent()
            )
    }

}