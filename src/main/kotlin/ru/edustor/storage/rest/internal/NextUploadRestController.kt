package ru.edustor.storage.rest.internal

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.edustor.commons.auth.annotation.RequiresAuth
import ru.edustor.storage.repository.AccountProfileRepository
import ru.edustor.storage.repository.getForAccountId

@RestController
@RequestMapping("api/v1/internal")
class NextUploadRestController(val accountProfileRepository: AccountProfileRepository) {
    @RequestMapping("nu", method = arrayOf(RequestMethod.POST))
    @RequiresAuth("internal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handlePdfUpload(@RequestParam("user_id") userId: String,
                        @RequestParam("target", required = false) targetLessonId: String?) {

        val uAccount = accountProfileRepository.getForAccountId(userId)
        uAccount.nextUploadTarget = targetLessonId
        accountProfileRepository.save(uAccount)
    }
}