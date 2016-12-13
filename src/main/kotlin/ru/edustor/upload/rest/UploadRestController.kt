package ru.edustor.upload.rest

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.commons.models.internal.accounts.EdustorAccount
import ru.edustor.commons.models.upload.UploadResult
import ru.edustor.upload.exception.InvalidContentTypeException
import ru.edustor.upload.exception.MaxFileSizeViolationException
import ru.edustor.upload.service.PagesUploadService

@RestController
@RequestMapping("api/v1/upload")
class UploadRestController(val uploadService: PagesUploadService) {
    val httpClient = OkHttpClient()

    @RequestMapping("pages", method = arrayOf(RequestMethod.POST))
    fun handlePdfUpload(@RequestParam("file") file: MultipartFile,
                        @RequestParam("target_lesson", required = false) targetLessonId: String?,
                        account: EdustorAccount): UploadResult? {

        account.assertScopeContains("upload")

        if (file.contentType != "application/pdf") {
            throw InvalidContentTypeException("This url is accepts only application/pdf documents")
        }

        val result = uploadService.processFile(account.uuid, file.inputStream, file.size, targetLessonId)
        return result
    }

    @RequestMapping("pages/url", method = arrayOf(RequestMethod.POST))
    fun handleUrlPdfUpload(@RequestParam url: String,
                           @RequestParam("target", required = false) targetLessonId: String?,
                           @RequestParam("uploader_id", required = false) requestedUploaderId: String?, // For internal usage
                           account: EdustorAccount): UploadResult {
        val uploaderId = if (requestedUploaderId != null) {
            account.assertScopeContains("internal")
            requestedUploaderId
        } else {
            account.assertScopeContains("upload")
            account.uuid
        }

        val req = Request.Builder().url(url).build()
        val resp = httpClient.newCall(req).execute()

        resp.body().use {
            val contentLength = resp.body().contentLength()
            if (contentLength > 100 * 1024 * 1024) {
                throw MaxFileSizeViolationException()
            } else if (contentLength == -1L) {
                throw MaxFileSizeViolationException("URL's server didn't return Content-Length")
            }

            val result = uploadService.processFile(uploaderId, resp.body().byteStream(), contentLength, targetLessonId)
            return result
        }
    }
}