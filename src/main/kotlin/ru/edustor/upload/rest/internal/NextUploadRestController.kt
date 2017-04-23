package ru.edustor.upload.rest.internal

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.edustor.commons.auth.annotation.RequiresScope
import ru.edustor.commons.auth.model.EdustorAuthProfile
import ru.edustor.upload.repository.AccountRepository
import ru.edustor.upload.repository.getForAccountId

@RestController
@RequestMapping("api/v1/internal")
class NextUploadRestController(val accountRepository: AccountRepository) {
    @RequestMapping("nu", method = arrayOf(RequestMethod.POST))
    @RequiresScope("internal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handlePdfUpload(@RequestParam("user_id") userId: String,
                        @RequestParam("target", required = false) targetLessonId: String?) {

        val uAccount = accountRepository.getForAccountId(userId)
        uAccount.nextUploadTarget = targetLessonId
        accountRepository.save(uAccount)
    }
}