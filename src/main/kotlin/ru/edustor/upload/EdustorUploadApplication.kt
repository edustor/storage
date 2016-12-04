package ru.edustor.upload

import org.springframework.boot.autoconfigure.SpringBootApplication

//TODO: Remove basePackages arg
@SpringBootApplication(scanBasePackages = arrayOf("ru.edustor.commons.auth", "ru.edustor.upload"))
open class EdustorUploadApplication