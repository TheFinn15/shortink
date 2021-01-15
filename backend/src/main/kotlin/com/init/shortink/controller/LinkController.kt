package com.init.shortink.controller

import com.init.shortink.model.Link
import com.init.shortink.repo.LinkRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@CrossOrigin
@RestController
@RequestMapping("/api")
class LinkController(
        val linkRepo: LinkRepo
) {

    @GetMapping("/link/all")
    private fun getAll() : ResponseEntity<List<Link>> {
        return ResponseEntity.ok(linkRepo.findAll())
    }

    @PostMapping("/link")
    private fun createLink(@RequestBody link: Link) : ResponseEntity<Any> {
        try {
            if (link.encryptLink == null || link.nativeLink == null) {
                throw Exception("Empty fields")
            }

            if (link.user !== null) {
                val checkExists: Long = linkRepo.checkUnique(link.encryptLink, link.user!!.id).toArray().size.toLong()
                if (checkExists > 0L) {
                    throw Exception("Duplicates find!")
                }
            }

            val dateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss")
            link.createdDate = dateTime.format(formatter)
            linkRepo.save(link)

            // linkRepo.getByEncryptLink(encLink = link.encryptLink)
            println(linkRepo.getByEncryptLink(link.encryptLink))
            return ResponseEntity.ok(linkRepo.getByEncryptLink(link.encryptLink))
        } catch (e: Exception) {
            val info: HashMap<String, String> = HashMap()
            info["status"] = "404"
            info["msg"] = "${e.message}"

            return ResponseEntity(info, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/link/{encryptLink}")
    private fun deleteLink(@RequestBody link: String) : ResponseEntity<Any> {
        try {
            val link: Link = linkRepo.getByEncryptLink(link)
            if (link !== null) {
                linkRepo.delete(link)
            } else {
                throw Exception()
            }
            return ResponseEntity.ok("Ссылка была удалена")
        } catch (e: Exception) {
            return ResponseEntity("Ссылка не была найдена", HttpStatus.BAD_REQUEST)
        }
    }
}