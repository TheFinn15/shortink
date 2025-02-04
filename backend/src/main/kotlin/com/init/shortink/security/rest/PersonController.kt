package com.init.shortink.security.rest

import com.init.shortink.security.model.Authority
import com.init.shortink.security.model.Person
import com.init.shortink.security.repo.PersonRepo
import com.init.shortink.security.service.PersonService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@CrossOrigin()
@RestController
@RequestMapping("/api")
class PersonController(
        private val personRepo: PersonRepo,
        private val personService: PersonService
) {

    @GetMapping("/users/all")
    private fun getAll(): ResponseEntity<MutableList<Person>> {
        return ResponseEntity.ok(personRepo.findAll())
    }

    @GetMapping("/user")
    private fun getCurrent(): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(personService.getUserWithAuthorities()!!.get())
        } catch (e: java.lang.Exception) {
            ResponseEntity.ok("Users not exists")
        }
    }

    @GetMapping("/users/{id}")
    private fun getById(@PathVariable id: Long): ResponseEntity<Any>  {
        try {
            return ResponseEntity.ok(personRepo.getOne(id))
        } catch (e: Exception) {
            return ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST)
        }
    }

//    @GetMapping("/users/{login}")
//    private fun getByLogin(@PathVariable login: String): ResponseEntity<Any> {
//        try {
//            return ResponseEntity.ok(personRepo.getByLogin(login))
//        } catch (e: Exception) {
//            return ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST)
//        }
//    }

    @PostMapping("/register")
    private fun createUser(@RequestBody person: Person): ResponseEntity<Any> {
        try {
            val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
            person.activated = true
            person.authorities= setOf(Authority("ROLE_USER"))
            person.pwd = encoder.encode(person.pwd)
            personRepo.save(person)
            return ResponseEntity(personRepo.getByLogin(person.login), HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Проверьте поля", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/check-auth")
    fun checkAuth(@Context req: HttpServletRequest) : ResponseEntity<Any> {
        val obj: HashMap<String, Int> = HashMap()

        return try {
            Jwts.parser()
                    .setSigningKey("ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=")
                    .parseClaimsJws(req.getHeader("token"))
                    .body
            obj["status"] = 200
            ResponseEntity(obj, HttpStatus.OK)
        } catch (e: java.lang.Exception) {
            obj["status"] = 400
            ResponseEntity(obj, HttpStatus.OK)
        }
    }

    @PutMapping("/users/{id}")
    private fun editUser(@PathVariable id: Long, @RequestBody person: Person): ResponseEntity<Any> {
        try {
            val curPerson: Person = personRepo.getOne(id)

            if (person.fName.isNotEmpty()) {
                curPerson.fName = person.fName
            }
            if (person.lName.isNotEmpty()) {
                curPerson.lName = person.lName
            }
            if (person.login.isNotEmpty()) {
                curPerson.login = person.login
            }
            if (person.email.isNotEmpty()) {
                curPerson.email = person.email
            }
            personRepo.setIsOnline(person.isOnline, id)
            personRepo.save(curPerson)
            return ResponseEntity.ok(personRepo.getByLogin(curPerson.login))
        } catch (e: Exception) {
            println(e.message)
            return ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/users/{id}")
    private fun delUser(@PathVariable id: Long) {
        personRepo.delete(personRepo.getOne(id))
    }
}