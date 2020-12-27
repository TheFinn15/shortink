package com.init.shortink

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortinkApplication

fun main(args: Array<String>) {
	runApplication<ShortinkApplication>(*args)
}
