package com.init.shortink.security.rest

import com.fasterxml.jackson.annotation.JsonProperty;
import com.init.shortink.security.repo.PersonRepo;
import com.init.shortink.security.jwt.JWTFilter;
import com.init.shortink.security.jwt.TokenProvider;
import com.init.shortink.security.rest.dto.LoginDTO;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/api")
class AuthController(
        private var authenticationManagerBuilder: AuthenticationManagerBuilder?,
        private var tokenProvider: TokenProvider?,
        private var jwtFilter: JWTFilter
) {

    @PostMapping("/auth")
    fun authorize(@Valid @RequestBody loginDto: LoginDTO): ResponseEntity<JWTToken> {
        val authenticationToken: UsernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(loginDto.login, loginDto.pwd)
        println("login: " + loginDto.login.toString() + " pass: " + loginDto.pwd)
        val authentication = authenticationManagerBuilder!!.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val rememberMe = loginDto.rememberMe ?: false
        val jwt: String = tokenProvider?.createToken(authentication, rememberMe)!!
        val httpHeaders = HttpHeaders()
        httpHeaders.add(jwtFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }



    /**
     * Object to return as body in JWT Authentication.
     */
    class JWTToken(idToken: String) {
        private var idToken: String
        @JsonProperty("id_token")
        fun getIdToken(): String {
            return idToken
        }

        fun setIdToken(idToken: String) {
            this.idToken = idToken
        }

        init {
            this.idToken = idToken
        }
    }
}