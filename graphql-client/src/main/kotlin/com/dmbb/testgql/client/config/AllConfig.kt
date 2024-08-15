package com.dmbb.testgql.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AllConfig {

    @Bean
    fun defaultClient(): RestClient = RestClient.create()

    @Bean
    fun graphQlClient(restClient: RestClient): HttpSyncGraphQlClient = HttpSyncGraphQlClient.create(restClient)
        .mutate()
        .url("http://localhost:8080/graphql")
        .build()

    @Bean
    fun webClient(): WebClient = WebClient.create("http://localhost:8080/graphql")


//    @Bean
//    fun webClient = WebClient

//    val customClient = RestClient.builder()
//        .requestFactory(HttpComponentsClientHttpRequestFactory())
//        .messageConverters { converters -> converters.add(MyCustomMessageConverter()) }
//        .baseUrl("https://example.com")
//        .defaultUriVariables(mapOf("variable" to "foo"))
//        .defaultHeader("My-Header", "Foo")
//        .requestInterceptor(myCustomInterceptor)
//        .requestInitializer(myCustomInitializer)
//        .build()

}