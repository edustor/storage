package ru.edustor.recognition.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
class InvalidContentTypeException(msg: String) : ServletException(msg)