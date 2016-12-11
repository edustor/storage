package ru.edustor.upload.rest.internal

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.commons.models.internal.accounts.EdustorAccount
import ru.edustor.upload.repository.AccountRepository
import ru.edustor.upload.repository.getForAccountId

@RestController
@RequestMapping("api/v1/internal")
class NextUploadRestController(val accountRepository: AccountRepository) {
    @RequestMapping("nu", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handlePdfUpload(@RequestParam("user_id") userId: String,
                        @RequestParam("target") targetLessonId: String,
                        account: EdustorAccount) {
        account.assertScopeContains("internal")

        val uAccount = accountRepository.getForAccountId(userId)
        uAccount.nextUploadTarget = targetLessonId
        accountRepository.save(uAccount)
    }
}