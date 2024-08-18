package com.dmbb.testgql.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.*
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.socket.client.StandardWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient

@Configuration
class AllConfig {

    private val baseUrlHttp = "http://localhost:8080/graphql"
    private val baseUrlWs = "ws://localhost:8080/graphql"

    @Bean
    fun defaultClient(): RestClient = RestClient.create()

    @Bean
    fun graphQlClientSync(restClient: RestClient): HttpSyncGraphQlClient = HttpSyncGraphQlClient.create(restClient)
        .mutate()
        .url(baseUrlHttp)
        .build()

    @Bean
    fun webClient(): WebClient = WebClient.create(baseUrlHttp)

    @Bean
    fun graphqlClient(webClient: WebClient) = HttpGraphQlClient.create(webClient)

    @Bean
    fun webSocketClient(): WebSocketClient = StandardWebSocketClient()

    @Bean
    fun graphqlClientWebSocket(webSocketClient: WebSocketClient) = WebSocketGraphQlClient.builder(baseUrlWs, webSocketClient).build()


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