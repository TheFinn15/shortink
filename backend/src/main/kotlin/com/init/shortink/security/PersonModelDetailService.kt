package com.init.shortink.security

import com.init.shortink.security.model.Person
import com.init.shortink.security.repo.PersonRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors


@Component("userDetailsService")
class PersonModelDetailService {
//    private val log: Logger = LoggerFactory.getLogger(com.newOnlinewWashing.security.UserModelDetailsService::class.java)

    private var userRepository: PersonRepo? = null

    fun UserModelDetailsService(userRepository: PersonRepo?) {
        this.userRepository = userRepository
    }

//    @Transactional
//    fun loadUserByUsername(login: String): UserDetails? {
//        log.debug("Authenticating user '{}'", login)
//        return if (EmailValidator().isValid(login, null)) {
//            userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
//                    .map { user -> createSpringSecurityUser(login, user) }
//                    .orElseThrow { UsernameNotFoundException("User with email $login was not found in the database") }
//        } else userRepository.findOneWithAuthoritiesByLogin(login)
//                .map { user -> createSpringSecurityUser(login, user) }
//                .orElseThrow { UsernameNotFoundException("User $login was not found in the database") }
//    }

//    private fun createSpringSecurityUser(lowercaseLogin: String, user: Person): User? {
//        if (!user.activated) {
//            throw UserNotActivatedException("User $lowercaseLogin was not activated")
//        }
//        val grantedAuthorities: List<GrantedAuthority> = user.authorities.stream()
//                .map { authority -> SimpleGrantedAuthority(authority.name) }
//                .collect(Collectors.toList())
//        return User(user.login,
//                user.pwd,
//                grantedAuthorities)
//    }
}