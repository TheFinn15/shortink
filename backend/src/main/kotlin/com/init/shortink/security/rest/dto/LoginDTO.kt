package com.init.shortink.security.rest.dto

class LoginDTO {

    fun LoginDto(username: String?, password: String?) {
        login = username
        pwd = password
    }

    var login: String? = null

    var pwd: String? = null

    val rememberMe: Boolean? = null
}