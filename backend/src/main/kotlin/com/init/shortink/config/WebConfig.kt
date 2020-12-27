package com.init.shortink.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
    }
}