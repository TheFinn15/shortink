package com.init.shortink.repo

import com.init.shortink.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PersonRepo : JpaRepository<Person, Long> {
    @Query("SELECT * FROM Person WHERE login = :login", nativeQuery = true)
    fun getByLogin(login: String) : Person
}