package com.dmbb.testgql.server.controller

import com.dmbb.testgql.server.model.Author
import com.dmbb.testgql.server.service.AuthorService
import kotlinx.coroutines.*
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.seconds

@Controller
class AuthorsController(private val authorService: AuthorService) {

    private val scope = CoroutineScope(Dispatchers.IO)

    @QueryMapping
    fun authorsGeneratedFlux(): Flux<Author> = authorService.generateAuthors(5).also {
        println("AuthorsController - return Flux")
    }

    @SubscriptionMapping
    fun authorsGeneratedSubs(): Flux<Author> = authorService.generateAuthors(5).also {
        println("AuthorsController - return Flux")
    }

    @QueryMapping
    fun authorsGenerated(): List<Author> {
//        val list =  mutableListOf<Author>()
//        authorsGeneratedFlux().subscribe {
//            list.add(it)
//        }
//        runBlocking { delay(3000) }
//        return list

        return authorsGeneratedFlux()
            .collectList()
            .block(Duration.of(4, ChronoUnit.SECONDS))!!
            .toList()
            .also { println("AuthorsController authorsGenerated returned list $it") }
    }

}