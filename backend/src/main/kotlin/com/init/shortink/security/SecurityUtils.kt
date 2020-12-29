package com.init.shortink.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


class SecurityUtils {
    private val LOG = LoggerFactory.getLogger(SecurityUtils::class.java)

    private fun SecurityUtils() {}

    fun getCurrentUsername(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null) {
            LOG.debug("No authentication in security context found")
            return Optional.empty()
        }
        var username: String? = null
        if (authentication.principal is UserDetails) {
            val springSecurityUser = authentication.principal as UserDetails
            username = springSecurityUser.username
        } else if (authentication.principal is String) {
            username = authentication.principal as String
        }
        LOG.debug("Found username '{}' in security context", username)
        return Optional.ofNullable(username)
    }
}