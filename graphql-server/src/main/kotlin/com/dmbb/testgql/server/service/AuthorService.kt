package com.dmbb.testgql.server.service

import com.dmbb.testgql.server.model.Author
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.lang.Thread.sleep

@Service
class AuthorService {

    val authors = listOf(
        Author("author-1", "Joshua", "Bloch"),
        Author("author-2", "Douglas", "Adams"),
        Author("author-3", "Bill", "Bryson")
    )

    fun getById(id: String): Author? = authors.find { it.id == id }

    fun generateAuthors(limit: Int) : Flux<Author> {
        return Flux.create { emitter ->
            repeat(limit) { i ->
                authors.random()
                    .copy(id = "author-$i")
                    .also {
                        println("produce $it")
                        emitter.next(it)
                    }
                sleep(500)
            }
            emitter.complete()
        }
    }

}
