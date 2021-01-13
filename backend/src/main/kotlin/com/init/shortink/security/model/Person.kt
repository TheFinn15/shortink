package com.init.shortink.security.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.BatchSize
import javax.persistence.*

@Table
@Entity
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0

    @Column
    var fName: String = ""

    @Column
    var lName: String = ""

    @Column(unique = true)
    var login: String = ""

    @Column
    var pwd: String = ""

    @Column
    var email: String = ""

    @Column(length = 1000000)
    var img: String = ""

    @JsonIgnore
    @Column
    var activated: Boolean = false

    @ManyToMany
    @JoinTable(name = "USER_AUTHORITY", joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "NAME")])
    @BatchSize(size = 20)
    var authorities: Set<Authority> = HashSet<Authority>()
}