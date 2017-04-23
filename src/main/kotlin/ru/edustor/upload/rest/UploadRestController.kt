package ru.edustor.upload.rest

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.edustor.commons.auth.annotation.RequiresScope
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.commons.auth.model.EdustorAuthProfile
import ru.edustor.commons.models.upload.UploadResult
import ru.edustor.upload.exception.InvalidContentTypeException
import ru.edustor.upload.exception.MaxFileSizeViolationException
import ru.edustor.upload.service.PagesUploadService

@RestController
@RequestMapping("api/v1/upload")
class UploadRestController(val uploadService: PagesUploadService) {
    val httpClient: OkHttpClient = OkHttpClient()

    @RequestMapping("pages", method = arrayOf(RequestMethod.POST))
    @RequiresScope("upload")
    fun handlePdfUpload(@RequestParam("file") file: MultipartFile,
                        @RequestParam("target_lesson", required = false) targetLessonId: String?,
                        authProfile: EdustorAuthProfile): UploadResult? {


        if (file.contentType != "application/pdf") {
            throw InvalidContentTypeException("This url is accepts only application/pdf documents")
        }

        val result = uploadService.processFile(authProfile.accountId, file.inputStream, file.size, targetLessonId)
        return result
    }

    @RequestMapping("pages/url", method = arrayOf(RequestMethod.POST))
    @RequiresScope("upload | internal")
    fun handleUrlPdfUpload(@RequestParam url: String,
                           @RequestParam("target", required = false) targetLessonId: String?,
                           @RequestParam("uploader_id", required = false) requestedUploaderId: String?, // For internal usage
                           authProfile: EdustorAuthProfile): UploadResult {
        val uploaderId = if (requestedUploaderId != null) {
            authProfile.assertScopeContains("internal")
            requestedUploaderId
        } else {
            authProfile.assertScopeContains("upload")
            authProfile.accountId
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