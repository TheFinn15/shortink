package com.init.shortink.repo

import com.init.shortink.model.Link
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LinkRepo : JpaRepository<Link, Long> {
    @Query("SELECT * FROM link WHERE encrypt_link = :encLink", nativeQuery = true)
    fun getByEncryptLink(encLink: String) : Link

    @Query("SELECT * FROM link WHERE encrypt_link = :encLink AND user_id = :userId", nativeQuery = true)
    fun checkUnique(encLink: String, userId: Long) : ArrayList<Link>
}