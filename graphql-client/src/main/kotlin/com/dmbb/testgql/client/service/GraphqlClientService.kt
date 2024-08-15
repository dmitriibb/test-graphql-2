package com.dmbb.testgql.client.service

import com.dmbb.testgql.client.model.Book
import org.springframework.beans.factory.InitializingBean
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.stereotype.Service

@Service
class GraphqlClientService(
    private val syncGraphQlClient: HttpSyncGraphQlClient,
) : InitializingBean {



    override fun afterPropertiesSet() {
        println("GraphqlClientService afterPropertiesSet()")
        queryBookSyncButNonBlocking("book-2")
        println("queryBookSync = ${queryBookSync("book-1")}")
        println("queryBookSync = ${queryBookSync("book-id-dont-exist")}")
    }

    fun queryBookSync(bookId: String): Book? {
        val document = getBookByIdDocument(bookId)

//        val book = syncGraphQlClient.document(document)
//            .retrieveSync("bookById")
//            .toEntity(Book::class.java)
        val response = syncGraphQlClient.document(document).executeSync()
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
        syncGraphQlClient.documentName("queryBookById")
            .variable("bookId", bookId)
            .retrieve("bookById")
            .toEntity(Book::class.java)
            .doOnCancel { println("queryBookSyncButNonBlocking - doOnCancel") }
            .subscribe {
                println("queryBookSyncButNonBlocking subscribe = $it")
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