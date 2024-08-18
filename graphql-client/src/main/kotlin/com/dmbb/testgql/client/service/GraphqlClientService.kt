package com.dmbb.testgql.client.service

import com.dmbb.testgql.client.model.Author
import com.dmbb.testgql.client.model.Book
import org.springframework.beans.factory.InitializingBean
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.graphql.client.WebSocketGraphQlClient
import org.springframework.stereotype.Service

@Service
class GraphqlClientService(
    private val graphQlClientSync: HttpSyncGraphQlClient,
    private val graphqlClientReact: HttpGraphQlClient,
    private val graphQlClientWebSocket: WebSocketGraphQlClient,
) : InitializingBean {



    override fun afterPropertiesSet() {
        println("GraphqlClientService afterPropertiesSet()")
        queryBookSyncButNonBlocking("book-2")
        println("queryBookSync = ${queryBookSync("book-1")}")
        println("queryBookSync = ${queryBookSync("book-id-dont-exist")}")
//        queryBookByIdReact("book-3")
        queryAuthorsGeneratedSubs()
//        queryAuthorsGeneratedFlux()
    }

    fun queryBookSync(bookId: String): Book? {
        val document = getBookByIdDocument(bookId)

//        val book = syncGraphQlClient.document(document)
//            .retrieveSync("bookById")
//            .toEntity(Book::class.java)
        val response = graphQlClientSync.document(document).executeSync()
        if (!response.isValid) {
            println("Error during queryBookSync because ${response.errors}")
            return null
        }

        val field = response.field("bookById")

        if (field.errors.isNotEmpty()) {
            println("Error during queryBookSync because ${field.errors}")
            return null
        } else {
            return field.toEntity(Book::class.java)
        }
    }

    fun queryBookSyncButNonBlocking(bookId: String) {
        graphQlClientSync.documentName("queryBookById")
            .variable("bookId", bookId)
            .retrieve("bookById")
            .toEntity(Book::class.java)
            .doOnCancel { println("queryBookSyncButNonBlocking - doOnCancel") }
            .subscribe {
                println("queryBookSyncButNonBlocking subscribe = $it")
            }
    }

    // doesn't work. Need to use websocket or manually do something with SSE
    // SSE will be in the next version
    // https://docs.spring.io/spring-graphql/reference/1.3-SNAPSHOT/transports.html#server.transports.sse
    fun queryBookByIdReact(bookId: String) {
        try {
            graphqlClientReact.documentName("queryBookByIdReactive")
                .variable("bookId", bookId)
//                .retrieveSubscription("bookByIdReactive")
//                .toEntity(Book::class.java)
//                .subscribe { book ->
//                    println("queryBookByIdReact = $book")
//                }
                .executeSubscription()
                .map { response ->
                    if (!response.isValid) {
                        println("Error during queryBookByIdReact because ${response.errors}")
                    } else {
                        val field = response.field("bookByIdReactive")
                        val book = field.toEntity(Book::class.java)
                        println("queryBookByIdReact $book")
                    }
                }.subscribe()
        } catch (ex:Exception) {
            println("Error during queryBookByIdReact because ${ex.message}")
        }
    }

    private fun queryAuthorsGeneratedSubs() {
        graphQlClientWebSocket.documentName("queryAuthorsGeneratedSubs")
//            .retrieveSubscription("authorsGeneratedSubs")
//            .toEntity(Author::class.java)
//            .subscribe {
//                println("queryAuthorsGeneratedSubs = $it")
//            }
            .executeSubscription()
            .subscribe { response ->
                if (response.errors.isNotEmpty()) {
                    println("queryAuthorsGeneratedSubs errors = ${response.errors}")
                } else {
                    val field = response.field("authorsGeneratedSubs").toEntity(Author::class.java)
                    println("queryAuthorsGeneratedSubs = $field")
                }
            }
    }

    private fun queryAuthorsGeneratedFlux() {
        graphQlClientWebSocket.documentName("queryAuthorsGeneratedFlux")
//            .executeSubscription().map { response ->
//                if (response.errors.isNotEmpty()) {
//                    println("authorsGeneratedFlux errors = ${response.errors}")
//                } else {
//                    val field = response.field("authorsGeneratedFlux").toEntityList(Author::class.java)
//                    println("authorsGeneratedFlux = $field")
//                }
//            }.subscribe()
//            .executeSubscription().doOnEach {
//                println("do on each ${it.get()}")
//            }
//            .limitRate(3)
//            .subscribe { response ->
//                val field = response.field("authorsGeneratedFlux").toEntityList(Author::class.java)
//                println("queryAuthorsGeneratedFlux = $field")
//            }
            .retrieveSubscription("authorsGeneratedFlux")
            .toEntityList(Author::class.java)
            .subscribe {
                println("queryAuthorsGeneratedFlux = $it")
            }
    }

    private fun getBookByIdDocument(bookId: String) = """
            {
                bookById(id: "$bookId") {
                    id
                    name
                    author {
                        id
                        firstName
                    }
                }
            }
        """

}