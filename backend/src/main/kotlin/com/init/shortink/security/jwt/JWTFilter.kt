package com.init.shortink.security.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JWTFilter(tokenProvider: TokenProvider?) : GenericFilterBean() {

    private val LOG = LoggerFactory.getLogger(JWTFilter::class.java)

    val AUTHORIZATION_HEADER = "Authorization"

    private var tokenProvider: TokenProvider = tokenProvider!!

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)
        val requestURI = httpServletRequest.requestURI
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            val authentication: Authentication? = tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            LOG.debug("set Authentication to security context for '{}', uri: {}", authentication?.name, requestURI)
        } else {
            LOG.debug("no valid JWT token found, uri: {}", requestURI)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

}