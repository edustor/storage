package ru.edustor.storage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.NOT_FOUND)
class DocumentNotFoundException(msg: String = "Cannot find requested document") : ServletException(msg)