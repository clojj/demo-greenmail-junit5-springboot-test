package com.example.demo

import javax.persistence.*

@Table(name = "user_entity")
@Entity
open class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    open var name: String? = null
}