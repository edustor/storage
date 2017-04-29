package ru.edustor.storage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class PageNotFoundException(msg: String) : ServletException(msg)