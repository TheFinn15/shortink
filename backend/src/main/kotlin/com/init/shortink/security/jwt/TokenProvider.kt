package com.init.shortink.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import kotlin.properties.Delegates


@Component
class TokenProvider(
        @Value("\${jwt.base64-secret}") base64Secret: String?,
        @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long,
        @Value("\${jwt.token-validity-in-seconds-for-remember-me}") tokenValidityInSecondsForRememberMe: Long) {
    private val log = LoggerFactory.getLogger(TokenProvider::class.java)

    private val AUTHORITIES_KEY = "auth"

    private var base64Secret: String = base64Secret!!
    private var tokenValidityInMilliseconds by Delegates.notNull<Long>()
    private var tokenValidityInMillisecondsForRememberMe by Delegates.notNull<Long>()

    private lateinit var key: Key

    init {
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
        this.tokenValidityInMillisecondsForRememberMe = tokenValidityInSecondsForRememberMe * 1000
        val keyBytes: ByteArray = Decoders.BASE64.decode(base64Secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    @Override
    fun afterPropertiesSet() {
        val keyBytes: ByteArray = Decoders.BASE64.decode(base64Secret)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createToken(authentication: Authentication, rememberMe: Boolean): String? {
        val authorities = authentication.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(","))
        val now = Date().time
        val validity: Date
        validity = if (rememberMe) {
            Date(now + tokenValidityInMillisecondsForRememberMe)
        } else {
            Date(now + tokenValidityInMilliseconds)
        }
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact()
    }

    fun getAuthentication(token: String?): Authentication? {
        val claims: Claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
        val authorities: Collection<GrantedAuthority> =
                claims.get(AUTHORITIES_KEY).toString().split(",").stream()
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        val principal = User(claims.getSubject(), "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken)
            return true
        } catch (e: SecurityException) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.")
            log.trace("Expired JWT token trace: {}", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT token.")
            log.trace("Unsupported JWT token trace: {}", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT token compact of handler are invalid.")
            log.trace("JWT token compact of handler are invalid trace: {}", e)
        }
        return false
    }
}