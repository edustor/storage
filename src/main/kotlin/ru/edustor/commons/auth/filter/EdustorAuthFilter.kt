package ru.edustor.commons.auth.filter

import io.jsonwebtoken.JwtException
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import ru.edustor.commons.auth.EdustorTokenValidator
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class EdustorAuthFilter(val validator: EdustorTokenValidator) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request !is HttpServletRequest || response !is HttpServletResponse) { // Response is checked to perform smart cast
            return chain.doFilter(request, response)
        }
        try {
            val tokenStr = request.getHeader("Authorization")
            val account = validator.validate(tokenStr)
            request.setAttribute("account", account)
        } catch (e: JwtException) {
            response.status = 401
            return
        }

        return chain.doFilter(request, response)
    }
}