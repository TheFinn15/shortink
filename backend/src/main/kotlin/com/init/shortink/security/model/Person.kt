package com.init.shortink.model

import lombok.NonNull
import javax.persistence.*

@Table
@Entity
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Int = 0

    @Column
    var fName: String = ""

    @Column
    var lName: String = ""

    @Column(unique = true)
    var login: String = ""
}