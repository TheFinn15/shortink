package com.init.shortink.security.repo

import com.init.shortink.security.model.Person
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

interface PersonRepo : JpaRepository<Person, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Person SET isOnline = :value WHERE id = :id")
    fun setIsOnline(value: Boolean, id: Long)

    @Query("SELECT * FROM Person WHERE login = :login", nativeQuery = true)
    fun getByLogin(login: String) : Person

    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesByLogin(username: String?): Optional<Person?>?

    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesByEmailIgnoreCase(email: String?): Optional<Person?>?
}