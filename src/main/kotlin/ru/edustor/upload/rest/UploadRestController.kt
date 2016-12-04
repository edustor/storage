package ru.edustor.upload.rest

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.commons.protobuf.proto.EdustorUploadApiProtos.UploadResult
import ru.edustor.commons.protobuf.proto.internal.EdustorAccountsProtos.EdustorAccount
import ru.edustor.commons.protobuf.proto.internal.EdustorPdfProcessingProtos.PdfUploadedEvent
import ru.edustor.commons.storage.service.BinaryObjectStorageService
import ru.edustor.commons.storage.service.BinaryObjectStorageService.ObjectType
import ru.edustor.upload.exception.InvalidContentTypeException
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("api/v1/upload")
class UploadRestController(val storage: BinaryObjectStorageService, val rabbitTemplate: RabbitTemplate) {
    @RequestMapping("pages", method = arrayOf(RequestMethod.POST))
    fun handlePdfUpload(@RequestParam("file") file: MultipartFile, account: EdustorAccount): UploadResult? {

        account.assertScopeContains("upload")

        if (file.contentType != "application/pdf") {
            throw InvalidContentTypeException("This url is accepts only application/pdf documents")
        }

        val uuid = UUID.randomUUID().toString()
        storage.put(ObjectType.PDF_UPLOAD, uuid, file.inputStream, file.size)

        val uploadedEvent = PdfUploadedEvent.newBuilder()
                .setUuid(uuid)
                .setTimestamp(Instant.now().epochSecond)
                .setUserId("")
                .build()

        rabbitTemplate.convertAndSend("internal.edustor", "uploaded.pdf.pages.processing", uploadedEvent.toByteArray())

        val result = UploadResult.newBuilder()
                .setUuid(uuid)
                .build()

        return result
    }
}