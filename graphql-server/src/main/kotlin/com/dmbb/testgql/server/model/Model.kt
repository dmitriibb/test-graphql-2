package com.dmbb.testgql.server.model

data class Book (
    val id : String,
    val name: String,
    val pageCount: Int,
    val author: String
)

data class Author(
    val id: String,
    val firstName: String,
    val lastName: String,
)