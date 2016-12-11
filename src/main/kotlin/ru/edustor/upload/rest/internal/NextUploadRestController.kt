package ru.edustor.upload.rest.internal

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.commons.models.internal.accounts.EdustorAccount

@RestController
@RequestMapping("api/v1/internal")
class NextUploadRestController {
    @RequestMapping("nu", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handlePdfUpload(@RequestParam("user_id") userId: String,
                        @RequestParam("target") targetLessonId: String,
                        account: EdustorAccount) {
        account.assertScopeContains("internal")

    }
}