package ru.edustor.upload.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.NO_CONTENT)
class NoPagesFoundException(msg: String = "There aren't any uploaded pages") : ServletException(msg)