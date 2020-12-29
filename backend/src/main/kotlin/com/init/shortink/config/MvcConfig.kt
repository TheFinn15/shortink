package com.init.shortink.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import java.rmi.registry.Registry

@Configuration
class MvcConfig {

    fun addCorsMaps(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
    }
}