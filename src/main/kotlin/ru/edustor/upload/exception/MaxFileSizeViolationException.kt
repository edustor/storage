package ru.edustor.upload.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class MaxFileSizeViolationException(msg: String = "Max file size exceeded") : ServletException(msg)