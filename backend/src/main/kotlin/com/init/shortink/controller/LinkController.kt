package com.init.shortink.controller

import com.init.shortink.model.Link
import com.init.shortink.repo.LinkRepo
import org.apache.tomcat.util.http.parser.HttpParser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

            linkRepo.save(link)

            return ResponseEntity.ok(linkRepo.getByEncryptLink(encLink = link.encryptLink))
        } catch (e: Exception) {
            return ResponseEntity("Обязательные поля пустые в теле объекта", HttpStatus.BAD_REQUEST)
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