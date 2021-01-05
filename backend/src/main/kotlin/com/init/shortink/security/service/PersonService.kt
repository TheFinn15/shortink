package com.init.shortink.security.service

import com.init.shortink.security.SecurityUtils
import com.init.shortink.security.model.Person
import com.init.shortink.security.repo.PersonRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class PersonService(
        personRepo: PersonRepo
) {
    @Autowired
    var userRepo: PersonRepo = personRepo


    private var userRepository: PersonRepo = personRepo

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getUserWithAuthorities(): Optional<Person?>? {
        return userRepository::findOneWithAuthoritiesByLogin.let { SecurityUtils().getCurrentUsername().flatMap(it) }
    }

    fun save(user: Person) {
//      log.info("IN UserServiceImpl save {}", user);
        userRepo!!.save(user)
    }
}