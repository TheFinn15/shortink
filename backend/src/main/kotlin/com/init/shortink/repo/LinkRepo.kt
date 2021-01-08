package com.init.shortink.repo

import com.init.shortink.model.Link
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LinkRepo : JpaRepository<Link, Long> {
    @Query("SELECT * FROM Link WHERE encryptLink = :encLink", nativeQuery = true)
    fun getByEncryptLink(encLink: String) : Link
}