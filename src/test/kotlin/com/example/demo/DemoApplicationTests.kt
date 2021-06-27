package com.example.demo

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    companion object {
        @JvmField
        @RegisterExtension
        val greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP)
                .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
                .withPerMethodLifecycle(false)
    }

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun test() {
        val user = NewUserRequest("Adam")

        val result = webTestClient.post().uri("/user")
                .body(Mono.just(user), NewUserRequest::class.java)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED)

        greenMail.waitForIncomingEmail(2000, 1)
        greenMail.receivedMessages.forEach { println(it.subject) }
    }

}
