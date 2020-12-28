package com.init.shortink.controller

import com.init.shortink.model.Person
import com.init.shortink.repo.PersonRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("api/users")
class PersonController (
        private val personRepo: PersonRepo
        ) {

    @GetMapping("/all")
    private fun getAll(): ResponseEntity<MutableList<Person>> {
        return ResponseEntity.ok(personRepo.findAll())
    }

    @GetMapping
    private fun getCurrent() {
        // get current user of token
    }

    @GetMapping("/{id}")
    private fun getById(@PathVariable id: Long): ResponseEntity<Any>  {
        try {
            return ResponseEntity.ok(personRepo.getOne(id))
        } catch (e: Exception) {
            return ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping
    private fun createUser(@RequestBody person: Person): String {
        try {
            personRepo.save(person)
            return ResponseEntity(personRepo.getByLogin(person.login), HttpStatus.OK).toString()
        } catch (e: Exception) {
            return ResponseEntity("Проверьте поля", HttpStatus.BAD_REQUEST).toString()
        }
    }

    @PutMapping("/{id}")
    private fun editUser(@PathVariable id: Long, @RequestBody person: Person): ResponseEntity<Any> {
        try {
            val curPerson: Person = personRepo.getOne(id)
            if (person.fName !== null) {
                curPerson.fName = person.fName
            } else if (person.lName !== null) {
                curPerson.lName = person.lName
            } else if (person.login !== null) {
                curPerson.login = person.login
            }
            personRepo.save(curPerson)
            return ResponseEntity.ok(personRepo.getByLogin(curPerson.login))
        } catch (e: Exception) {
            return ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    private fun delUser(@PathVariable id: Long) {
        personRepo.delete(personRepo.getOne(id))
    }
}