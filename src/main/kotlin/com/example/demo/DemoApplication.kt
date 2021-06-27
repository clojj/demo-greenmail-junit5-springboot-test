package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@RestController
class DemoController(private val demoService: DemoService, private val javaMailSender: JavaMailSender) {

    @PostMapping("/user", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser(@RequestBody newUserRequest: NewUserRequest): ResponseEntity<Long> {
        val id = demoService.createUser(newUserRequest)
        val mail = SimpleMailMessage().apply {
            setFrom("admin@spring.io")
            setSubject("something")
            setText("hello")
            setTo("abc@def.com")
        }
        javaMailSender.send(mail)
        return ResponseEntity.created(URI.create("/user/$id")).build()
    }
}

data class NewUserRequest(val name: String)

@Service
class DemoService(private val userEntityRepository: UserEntityRepository) {

    fun createUser(newUserRequest: NewUserRequest): Long {
        val userEntity = UserEntity()
        userEntity.name = newUserRequest.name
        val entity = userEntityRepository.save(userEntity)
        return entity.id!!
    }
}