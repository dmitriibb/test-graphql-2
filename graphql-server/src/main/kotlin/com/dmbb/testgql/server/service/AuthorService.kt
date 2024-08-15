package com.dmbb.testgql.server.service

import com.dmbb.testgql.server.model.Author
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service

@Service
class AuthorService {

    val authors = listOf(
        Author("author-1", "Joshua", "Bloch"),
        Author("author-2", "Douglas", "Adams"),
        Author("author-3", "Bill", "Bryson")
    )

    fun getById(id: String): Author? = authors.find { it.id == id }

    fun generateAuthors() : Flow<Author> {
        return flow {
            repeat(100) { i ->
                authors.random()
                    .copy(id = "author-$i")
                    .also { println("produce $it") }
                    .let { emit(it) }
                delay(500)
            }
        }
    }

}
