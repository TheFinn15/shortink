package com.init.shortink.model

import javax.persistence.*

@Table
@Entity
class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    @Column
    val encryptLink: String = ""

    @Column
    val multiple: Boolean = false

    @Column
    val private: Boolean = false
}