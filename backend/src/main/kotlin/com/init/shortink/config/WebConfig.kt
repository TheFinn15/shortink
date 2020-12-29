package com.init.shortink.config

import com.init.shortink.security.JwtAccessDeniedHandler
import com.init.shortink.security.JwtAuthenticationEntryPoint
import com.init.shortink.security.jwt.JWTConfigurer
import com.init.shortink.security.jwt.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebConfig(
        tokenProvider: TokenProvider?,
        corsFilter: CorsFilter?,
        authenticationErrorHandler: JwtAuthenticationEntryPoint?,
        jwtAccessDeniedHandler: JwtAccessDeniedHandler?
) : WebSecurityConfigurerAdapter() {

    private final var tokenProvider: TokenProvider = tokenProvider!!

    private final var corsFilter: CorsFilter = corsFilter!!
    private final var authenticationErrorHandler: JwtAuthenticationEntryPoint = authenticationErrorHandler!!
    private final var jwtAccessDeniedHandler: JwtAccessDeniedHandler = jwtAccessDeniedHandler!!

//    init {
//        //        this.corsFilter = corsFilter!!
//    }

    // Configure BCrypt password encoder =====================================================================
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    // Configure paths and requests that should be ignored by Spring Security ================================
    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**") // allow anonymous resource requests
                .antMatchers(
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/h2-console/**"
                )
    }

    // Configure security settings ===========================================================================
    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler) // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin() // create no session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/persons").hasAuthority("ROLE_USER")
                .antMatchers("/api/hiddenmessage").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and().cors()
                .and()
                .apply(securityConfigurerAdapter())
    }

    private fun securityConfigurerAdapter(): JWTConfigurer {
        return JWTConfigurer(tokenProvider)
    }

}