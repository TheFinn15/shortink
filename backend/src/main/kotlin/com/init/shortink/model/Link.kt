package com.init.shortink.model

import com.init.shortink.security.model.Person
import org.springframework.format.annotation.DateTimeFormat
import javax.persistence.*

@Table
@Entity
class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column
    var encryptLink: String = ""

    @Column
    var nativeLink: String = ""

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: Person? = null

    @Column
    var multiple: Boolean = false

    @Column
    var private: Boolean = false

    @Column
    var createdDate: String = ""
}